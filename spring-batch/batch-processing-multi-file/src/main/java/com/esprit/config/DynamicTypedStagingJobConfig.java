package com.esprit.config;

import com.esprit.domain.model.StgCustomer;
import com.esprit.domain.model.StgOrder;
import lombok.RequiredArgsConstructor;
import org.springframework.batch.core.repository.JobRepository;
import org.springframework.context.annotation.Configuration;
import org.springframework.transaction.PlatformTransactionManager;

import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.job.builder.JobBuilder;
import org.springframework.batch.core.job.builder.SimpleJobBuilder;
import org.springframework.batch.core.step.builder.StepBuilder;
import org.springframework.batch.item.file.FlatFileItemReader;
import org.springframework.batch.item.file.mapping.DefaultLineMapper;
import org.springframework.batch.item.file.transform.DelimitedLineTokenizer;
import org.springframework.batch.item.database.BeanPropertyItemSqlParameterSourceProvider;
import org.springframework.batch.item.database.JdbcBatchItemWriter;
import org.springframework.context.annotation.Bean;
import org.springframework.core.io.FileSystemResource;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.batch.repeat.RepeatStatus;

import javax.sql.DataSource;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;

@Configuration
@RequiredArgsConstructor
public class DynamicTypedStagingJobConfig {

    private final JobRepository jobRepository;
    private final PlatformTransactionManager transactionManager;
    private final DataSource dataSource;


    /* ---------- 1) Définition générique d’un import ---------- */

    /**
     * ImportDef<T>
     * @param name    nom logique (utilisé dans le nom du step : "import-" + name)
     * @param file    chemin du CSV (avec header)
     * @param columns noms des colonnes dans le CSV (doivent matcher le FieldSetMapper)
     * @param mapper  fonction de mapping CSV -> DTO typé
     * @param upsertSql SQL d’upsert vers la table staging (params = getters du DTO)
     */
    public record ImportDef<T>(
            String name,
            String file,
            String[] columns,
            FieldSetMapperFn<T> mapper,
            String upsertSql
    ) {}

    /** Functional interface simple pour éviter de dépendre de l’API interne */
    @FunctionalInterface
    public interface FieldSetMapperFn<T> {
        T map(org.springframework.batch.item.file.transform.FieldSet fs) throws Exception;
    }

    /* ---------- 2) Catalogue d’imports (à externaliser YAML/DB en prod) ---------- */

    private List<ImportDef<?>> catalog() {
        DateTimeFormatter DMY = DateTimeFormatter.ofPattern("dd-MM-yyyy");

        ImportDef<StgCustomer> customers = new ImportDef<>(
                "customers",
                "src/main/resources/customers.csv",
                new String[]{"id","firstName","lastName","email","gender","contactNo","country","dob"},
                fs -> {
                    StgCustomer c = new StgCustomer();
                    c.setId(fs.readLong("id"));
                    c.setFirstName(fs.readString("firstName"));
                    c.setLastName(fs.readString("lastName"));
                    c.setEmail(fs.readString("email"));
                    c.setGender(fs.readString("gender"));
                    c.setContactNo(fs.readString("contactNo"));
                    c.setCountry(fs.readString("country"));
                    c.setDob(LocalDate.parse(fs.readString("dob"), DMY));
                    return c;
                },
                """
                INSERT INTO stg_customer (customer_id, first_name, last_name, email, gender, contact, country, dob)
                VALUES (:id, :firstName, :lastName, :email, :gender, :contactNo, :country, :dob)
                ON CONFLICT (customer_id) DO UPDATE SET
                  first_name = EXCLUDED.first_name,
                  last_name  = EXCLUDED.last_name,
                  email      = EXCLUDED.email,
                  gender     = EXCLUDED.gender,
                  contact    = EXCLUDED.contact,
                  country    = EXCLUDED.country,
                  dob        = EXCLUDED.dob
                """
        );

        ImportDef<StgOrder> orders = new ImportDef<>(
                "orders",
                "src/main/resources/orders.csv",
                new String[]{"id","customerId","orderDate","amount","status"},
                fs -> {
                    StgOrder o = new StgOrder();
                    o.setId(fs.readLong("id"));
                    o.setCustomerId(fs.readLong("customerId"));
                    o.setOrderDate(LocalDate.parse(fs.readString("orderDate"), DMY));
                    o.setAmount(new BigDecimal(fs.readString("amount")));
                    o.setStatus(fs.readString("status"));
                    return o;
                },
                """
                INSERT INTO stg_order (order_id, customer_id, order_date, amount, status)
                VALUES (:id, :customerId, :orderDate, :amount, :status)
                ON CONFLICT (order_id) DO UPDATE SET
                  customer_id = EXCLUDED.customer_id,
                  order_date  = EXCLUDED.order_date,
                  amount      = EXCLUDED.amount,
                  status      = EXCLUDED.status
                """
        );

        return List.of(customers, orders);
    }

    /* ---------- 3) Fabriques : reader / writer / step ---------- */

    private <T> FlatFileItemReader<T> buildReader(ImportDef<T> def) {
        FlatFileItemReader<T> reader = new FlatFileItemReader<>();
        reader.setName("reader-" + def.name());
        reader.setResource(new FileSystemResource(def.file()));
        reader.setLinesToSkip(1);

        DefaultLineMapper<T> lm = new DefaultLineMapper<>();

        DelimitedLineTokenizer tok = new DelimitedLineTokenizer();
        tok.setDelimiter(DelimitedLineTokenizer.DELIMITER_COMMA);
        tok.setStrict(false);
        tok.setNames(def.columns());

        lm.setLineTokenizer(tok);
        // try {
        //     lm.setFieldSetMapper(def.mapper()::map);
        // } catch (Exception e) {
        //
        // }
        // lm.setFieldSetMapper(fs -> def.mapper().map(fs));

        lm.setFieldSetMapper(fs -> {
            try {
                return def.mapper().map(fs);
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        });

        reader.setLineMapper(lm);
        return reader;
    }

    private <T> JdbcBatchItemWriter<T> buildWriter(ImportDef<T> def) {
        JdbcBatchItemWriter<T> w = new JdbcBatchItemWriter<>();
        w.setDataSource(dataSource);
        w.setSql(def.upsertSql());
        w.setItemSqlParameterSourceProvider(new BeanPropertyItemSqlParameterSourceProvider<>());
        w.afterPropertiesSet();
        return w;
    }

    private <T> Step buildStep(ImportDef<T> def) {
        return new StepBuilder("import-" + def.name(), jobRepository)
                .<T, T>chunk(1000, transactionManager)
                .reader(buildReader(def))
                .writer(buildWriter(def))
                .build();
    }

    /* ---------- 4) Step technique : truncate staging ---------- */

    @Bean
    public Step truncateTypedStagingStep() {
        return new StepBuilder("truncate-staging", jobRepository)
                .tasklet((contribution, chunkContext) -> {
                    JdbcTemplate jdbc = new JdbcTemplate(dataSource);
                    // Ajoute ici toutes tes tables de staging
                    jdbc.execute("TRUNCATE TABLE stg_order, stg_customer RESTART IDENTITY CASCADE");
                    return RepeatStatus.FINISHED;
                }, transactionManager)
                .build();
    }

    /* ---------- 5) Job dynamique linéaire ---------- */

    @Bean
    public Job importToStaging() {
        List<Step> importSteps = catalog().stream()
                .map(this::buildStep)
                .toList();

        JobBuilder jb = new JobBuilder("importToStaging", jobRepository);

        if (importSteps.isEmpty()) {
            return jb.start(truncateTypedStagingStep()).build();
        }

        SimpleJobBuilder sjb = jb.start(truncateTypedStagingStep());
        for (Step s : importSteps) {
            sjb = sjb.next(s);
        }
        return sjb.build();
    }
}
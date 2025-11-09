package com.esprit.config;

import com.esprit.config.config.BatchIdEnricherProcessor;
import com.esprit.domain.model.StgCustomer;
import com.esprit.domain.model.StgOrder;
import lombok.RequiredArgsConstructor;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.job.builder.FlowBuilder;
import org.springframework.batch.core.job.builder.JobBuilder;
import org.springframework.batch.core.job.flow.Flow;
import org.springframework.batch.core.repository.JobRepository;
import org.springframework.batch.core.step.builder.StepBuilder;
import org.springframework.batch.item.database.BeanPropertyItemSqlParameterSourceProvider;
import org.springframework.batch.item.database.JdbcBatchItemWriter;
import org.springframework.batch.item.file.FlatFileItemReader;
import org.springframework.batch.item.file.mapping.DefaultLineMapper;
import org.springframework.batch.item.file.transform.DelimitedLineTokenizer;
import org.springframework.batch.item.file.transform.FieldSet;
import org.springframework.batch.repeat.RepeatStatus;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.FileSystemResource;
import org.springframework.core.task.TaskExecutor;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.transaction.PlatformTransactionManager;

import javax.sql.DataSource;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Map;

@Configuration
@RequiredArgsConstructor
public class DynamicTypedStagingJobConfigParalelStep {

    private final JobRepository jobRepository;
    private final PlatformTransactionManager transactionManager;
    private final DataSource dataSource;


    /* ---------- 1) Définition générique d’un import ---------- */

    /**
     * ImportDef<T>
     *
     * @param name      nom logique (utilisé dans le nom du step : "import-" + name)
     * @param file      chemin du CSV (avec header)
     * @param columns   noms des colonnes dans le CSV (doivent matcher le FieldSetMapper)
     * @param mapper    fonction de mapping CSV -> DTO typé
     * @param upsertSql SQL d’upsert vers la table staging (params = getters du DTO)
     */
    public record ImportDef<T>(
            String name,
            String file,
            String[] columns,
            FieldSetMapperFn<T> mapper,
            String upsertSql
    ) {
    }

    /**
     * Functional interface simple pour éviter de dépendre de l’API interne
     */
    @FunctionalInterface
    public interface FieldSetMapperFn<T> {
        T map(FieldSet fs) throws Exception;
    }

    /* ---------- 2) Catalogue d’imports (à externaliser YAML/DB en prod) ---------- */

    private List<ImportDef<?>> catalog() {
        DateTimeFormatter DMY = DateTimeFormatter.ofPattern("dd-MM-yyyy");

        ImportDef<StgCustomer> customers = new ImportDef<>(
                "customers",
                "src/main/resources/customers.csv",
                new String[]{"id", "firstName", "lastName", "email", "gender", "contactNo", "country", "dob"},
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
                        INSERT INTO stg_customer (batch_id, customer_id, first_name, last_name, email, gender, contact, country, dob)
                        VALUES (:batchId, :id, :firstName, :lastName, :email, :gender, :contactNo, :country, :dob)
                        ON CONFLICT (batch_id, customer_id) DO UPDATE SET
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
                new String[]{"id", "customerId", "orderDate", "amount", "status"},
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
                        INSERT INTO stg_order (batch_id, order_id, customer_id, order_date, amount, status)
                        VALUES (:batchId, :id, :customerId, :orderDate, :amount, :status)
                        ON CONFLICT (batch_id, order_id) DO UPDATE SET
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
                .processor(new BatchIdEnricherProcessor<>())     // <- ajoute le batchId à chaque item
                .writer(buildWriter(def))
                .build();
    }

    /* ---------- 4) Step technique : truncate staging ---------- */

    @Bean
    public Step truncateTypedStagingStepParalel() {
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
    public TaskExecutor stepTaskExecutor() {
        var t = new ThreadPoolTaskExecutor();
        t.setThreadNamePrefix("import-");
        t.setCorePoolSize(4);        // <- limite le parallélisme
        t.setMaxPoolSize(4);
        t.setQueueCapacity(0);
        t.initialize();
        return t;
    }

    /*
    @Bean
    public Job importToStagingParalelStep() {
        List<Step> importSteps = catalog().stream()
                .map(this::buildStep)
                .toList();

        JobBuilder jb = new JobBuilder("importToStaging", jobRepository);

        if (importSteps.isEmpty()) {
            return jb.start(truncateTypedStagingStepParalel()).build();
        }

        // Chaque step -> flow indépendant
        List<Flow> flows = importSteps.stream()
                .map(s -> new FlowBuilder<Flow>(s.getName() + "Flow").start(s).end())
                .toList();

        // Flow maître: truncate puis split parallèle
        Flow master =
                new FlowBuilder<Flow>("masterFlow")
                        .start(truncateTypedStagingStepParalel())
                        .split(stepTaskExecutor())              // <-- parallélisme ici
                        .add(flows.toArray(new Flow[0]))
                        .build();

        return jb
                .start(master) // <-- on démarre le job avec un Flow
                .end()
                .build();
    }

     */

    /* ---------- 6) FLOW d’import (réutilisable) ---------- */
    @Bean
    public Flow importStagingFlow() {
        List<Step> importSteps = catalog().stream().map(this::buildStep).toList();

        // Chaque step -> flow indépendant
        List<Flow> flows = importSteps.stream()
                .map(s -> new FlowBuilder<Flow>(s.getName() + "Flow").start(s).end())
                .toList();

        // Flow maître: truncate puis split parallèle
        return new FlowBuilder<Flow>("importStagingFlow")
                .start(truncateTypedStagingStepParalel())
                .split(stepTaskExecutor())
                .add(flows.toArray(new Flow[0]))
                .build();
    }


    /* ---------- 7) Step de nettoyage + rejets ---------- */

    @Bean
    public NamedParameterJdbcTemplate namedParameterJdbcTemplate() {
        return new NamedParameterJdbcTemplate(dataSource);
    }

    @Bean
    public Step cleanAndRejectStep() {
        return new StepBuilder("cleanAndRejectStep", jobRepository)
                .tasklet((contribution, ctx) -> {
                    String batchId = String.valueOf(ctx.getStepContext().getJobParameters().get("batchId"));
                    var p = Map.of("batchId", batchId);

                    // Reset (idempotence)
                    namedParameterJdbcTemplate().update(
                            "UPDATE stg_customer SET valid_flag=TRUE, error_code=NULL, error_msg=NULL WHERE batch_id=:batchId", p);
                    namedParameterJdbcTemplate().update(
                            "UPDATE stg_order    SET valid_flag=TRUE, error_code=NULL, error_msg=NULL WHERE batch_id=:batchId", p);

                    // Règles stg_customer
                    namedParameterJdbcTemplate().update("""
            UPDATE stg_customer
               SET valid_flag = FALSE, error_code = 'EMAIL_FORMAT', error_msg = 'Email invalide'
             WHERE batch_id = :batchId AND valid_flag = TRUE
               AND (email IS NULL OR email !~ '^[^@\\s]+@[^@\\s]+\\.[^@\\s]+$')
          """, p);

                    namedParameterJdbcTemplate().update("""
            UPDATE stg_customer
               SET valid_flag = FALSE, error_code = 'GENDER_INVALID', error_msg = 'Valeur non autorisée'
             WHERE batch_id = :batchId AND valid_flag = TRUE
               AND COALESCE(gender,'') NOT IN ('Male','Female','Other','Unknown')
          """, p);

                    namedParameterJdbcTemplate().update("""
            UPDATE stg_customer
               SET valid_flag = FALSE, error_code = 'DOB_FUTURE', error_msg = 'Date de naissance future'
             WHERE batch_id = :batchId AND valid_flag = TRUE
               AND dob > CURRENT_DATE
          """, p);

                    namedParameterJdbcTemplate().update("""
            UPDATE stg_customer
               SET valid_flag = FALSE, error_code = 'DOB_AGE_RANGE', error_msg = 'Âge > 120 ans'
             WHERE batch_id = :batchId AND valid_flag = TRUE
               AND dob IS NOT NULL AND dob < CURRENT_DATE - INTERVAL '120 years'
          """, p);

                    // Règles stg_order
                    namedParameterJdbcTemplate().update("""
            UPDATE stg_order
               SET valid_flag = FALSE, error_code = 'AMOUNT_NEG', error_msg = 'Montant négatif'
             WHERE batch_id = :batchId AND valid_flag = TRUE
               AND amount IS NOT NULL AND amount < 0
          """, p);

                    namedParameterJdbcTemplate().update("""
            UPDATE stg_order
               SET valid_flag = FALSE, error_code = 'STATUS_INVALID', error_msg = 'Valeur non autorisée'
             WHERE batch_id = :batchId AND valid_flag = TRUE
               AND COALESCE(status,'') NOT IN ('NEW','PAID','CANCELLED','SHIPPED','REFUNDED')
          """, p);

                    namedParameterJdbcTemplate().update("""
            UPDATE stg_order
               SET valid_flag = FALSE, error_code = 'DATE_FUTURE', error_msg = 'Date future'
             WHERE batch_id = :batchId AND valid_flag = TRUE
               AND order_date > CURRENT_DATE
          """, p);

                    // Pré-validation FK (contre staging customers valides du même batch)
                    namedParameterJdbcTemplate().update("""
            UPDATE stg_order o
               SET valid_flag = FALSE, error_code = 'FK_CUSTOMER', error_msg = 'Customer inexistant'
             WHERE o.batch_id = :batchId AND o.valid_flag = TRUE
               AND NOT EXISTS (
                 SELECT 1 FROM stg_customer c
                  WHERE c.batch_id = o.batch_id
                    AND c.valid_flag = TRUE
                    AND c.customer_id = o.customer_id
               )
          """, p);

                    // Traçage des rejets
                    namedParameterJdbcTemplate().update("DELETE FROM rejets WHERE batch_id = :batchId", p);

                    namedParameterJdbcTemplate().update("""
            INSERT INTO rejets (batch_id, source_table, source_pk, error_code, error_msg, payload_json)
            SELECT :batchId, 'stg_customer', CAST(customer_id AS VARCHAR), error_code, error_msg, to_jsonb(s.*)
              FROM stg_customer s
             WHERE s.batch_id = :batchId AND s.valid_flag = FALSE AND s.error_code IS NOT NULL
          """, p);

                    namedParameterJdbcTemplate().update("""
            INSERT INTO rejets (batch_id, source_table, source_pk, error_code, error_msg, payload_json)
            SELECT :batchId, 'stg_order', CAST(order_id AS VARCHAR), error_code, error_msg, to_jsonb(o.*)
              FROM stg_order o
             WHERE o.batch_id = :batchId AND o.valid_flag = FALSE AND o.error_code IS NOT NULL
          """, p);

                    return RepeatStatus.FINISHED;
                }, transactionManager)
                .build();
    }

    /* ---- */

    @Bean
    public Step insertTargetsStep() {
        return new StepBuilder("insertTargetsStep", jobRepository)
                .tasklet((contribution, ctx) -> {
                    String batchId = String.valueOf(ctx.getStepContext().getJobParameters().get("batchId"));
                    var p = Map.of("batchId", batchId);

                    // 1) CUSTOMER : upsert depuis la staging valide du batch
                    int insCustomers = namedParameterJdbcTemplate().update("""
            INSERT INTO customer (customer_id, first_name, last_name, email, gender, contact, country, dob, created_at, updated_at)
            SELECT DISTINCT s.customer_id, s.first_name, s.last_name, s.email, s.gender, s.contact, s.country, s.dob, now(), now()
              FROM stg_customer s
             WHERE s.batch_id = :batchId
               AND s.valid_flag = TRUE
            ON CONFLICT (customer_id) DO UPDATE SET
              first_name = EXCLUDED.first_name,
              last_name  = EXCLUDED.last_name,
              email      = EXCLUDED.email,
              gender     = EXCLUDED.gender,
              contact    = EXCLUDED.contact,
              country    = EXCLUDED.country,
              dob        = EXCLUDED.dob,
              updated_at = now()
          """, p);

                    // 2) ORDERS : upsert si parent existe (sécurité supplémentaire)
                    int insOrders = namedParameterJdbcTemplate().update("""
            INSERT INTO orders (order_id, customer_id, order_date, amount, status, created_at, updated_at)
            SELECT o.order_id, o.customer_id, o.order_date, o.amount, o.status, now(), now()
              FROM stg_order o
             WHERE o.batch_id = :batchId
               AND o.valid_flag = TRUE
               AND EXISTS (SELECT 1 FROM customer c WHERE c.customer_id = o.customer_id)
            ON CONFLICT (order_id) DO UPDATE SET
              customer_id = EXCLUDED.customer_id,
              order_date  = EXCLUDED.order_date,
              amount      = EXCLUDED.amount,
              status      = EXCLUDED.status,
              updated_at  = now()
          """, p);

                    // (optionnel) log technique
                    contribution.incrementWriteCount(insCustomers + insOrders);
                    return RepeatStatus.FINISHED;
                }, transactionManager)
                .build();
    }

    /* ---------- 8) Job 1 (standalone) : Import seulement ---------- */
    @Bean
    public Job importToStagingJob() {
        return new JobBuilder("importToStaging", jobRepository)
                .start(importStagingFlow())   // le flow d’import (truncate + split)
                .end()
                .build();
    }

    /* ---------- 9) Job 2 (orchestration) : Import → Clean ---------- */
    @Bean
    public Job importThenCleanJob() {
        return new JobBuilder("importThenClean", jobRepository)
                .start(importStagingFlow())   // on réutilise exactement le même flow d’import
                .next(cleanAndRejectStep())   // puis le step de nettoyage + rejets
                .end()
                .build();
    }


    @Bean
    public Job importCleanInsertJob() {
        return new JobBuilder("importCleanInsert", jobRepository)
                .start(importStagingFlow())  // 1) Import CSV -> staging (parallèle)
                .next(cleanAndRejectStep())  // 2) Nettoyage + rejets
                .next(insertTargetsStep())   // 3) Insertion dans les cibles
                .end()
                .build();
    }

}

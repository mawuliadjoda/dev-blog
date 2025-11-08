package com.esprit.config;


import com.esprit.domain.model.StgCustomer;
import com.esprit.domain.model.StgOrder;
import lombok.RequiredArgsConstructor;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.job.builder.JobBuilder;
import org.springframework.batch.core.repository.JobRepository;
import org.springframework.batch.core.step.builder.StepBuilder;
import org.springframework.batch.core.step.tasklet.Tasklet;
import org.springframework.batch.item.database.BeanPropertyItemSqlParameterSourceProvider;
import org.springframework.batch.item.database.JdbcBatchItemWriter;
import org.springframework.batch.item.file.FlatFileItemReader;
import org.springframework.batch.item.file.LineMapper;
import org.springframework.batch.item.file.mapping.BeanWrapperFieldSetMapper;
import org.springframework.batch.item.file.mapping.DefaultLineMapper;
import org.springframework.batch.item.file.transform.DelimitedLineTokenizer;
import org.springframework.batch.repeat.RepeatStatus;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.FileSystemResource;
import org.springframework.format.support.DefaultFormattingConversionService;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.transaction.PlatformTransactionManager;

import javax.sql.DataSource;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

@Configuration
@RequiredArgsConstructor
public class BatchConfig {

    // ---------- READERS ----------
    @Bean
    public FlatFileItemReader<StgCustomer> customerReader() {
        FlatFileItemReader<StgCustomer> r = new FlatFileItemReader<>();
        r.setResource(new FileSystemResource("src/main/resources/customers.csv"));
        r.setName("customersCsvReader");
        r.setLinesToSkip(1);
        r.setLineMapper(customerLineMapper());
        return r;
    }

    private LineMapper<StgCustomer> customerLineMapper() {
        var lm = new DefaultLineMapper<StgCustomer>();

        var tok = new DelimitedLineTokenizer();
        tok.setDelimiter(",");
        tok.setStrict(false);
        tok.setNames("id", "firstName", "lastName", "email", "gender", "contactNo", "country", "dob");

        // var fsm = new BeanWrapperFieldSetMapper<StgCustomer>() {{
        //     setTargetType(StgCustomer.class);
        //     // si besoin: setCustomEditors pour LocalDate, etc. sinon Spring gère ISO-8601
        // }};

        BeanWrapperFieldSetMapper<StgCustomer> fsm = new BeanWrapperFieldSetMapper<>();
        fsm.setTargetType(StgCustomer.class);

        var cs = getDefaultFormattingConversionService();

        fsm.setConversionService(cs);


        lm.setLineTokenizer(tok);
        lm.setFieldSetMapper(fsm);
        return lm;
    }

    @Bean
    public FlatFileItemReader<StgOrder> orderReader() {
        FlatFileItemReader<StgOrder> r = new FlatFileItemReader<>();
        r.setResource(new FileSystemResource("src/main/resources/orders.csv"));
        r.setName("ordersCsvReader");
        r.setLinesToSkip(1);
        r.setLineMapper(orderLineMapper());
        return r;
    }

    private LineMapper<StgOrder> orderLineMapper() {
        var lm = new DefaultLineMapper<StgOrder>();

        var tok = new DelimitedLineTokenizer();
        tok.setDelimiter(",");
        tok.setStrict(false);
        tok.setNames("id", "customerId", "orderDate", "amount", "status");

        // var fsm = new BeanWrapperFieldSetMapper<StgOrder>() {{
        //     setTargetType(StgOrder.class);
        //     // si format de date/decimal spécifique, ajouter PropertyEditors/Converters
        // }};
        BeanWrapperFieldSetMapper<StgOrder> fsm = new BeanWrapperFieldSetMapper<>();
        fsm.setTargetType(StgOrder.class);

        var cs = getDefaultFormattingConversionService();

        fsm.setConversionService(cs);



        lm.setLineTokenizer(tok);
        lm.setFieldSetMapper(fsm);
        return lm;
    }

    private static DefaultFormattingConversionService getDefaultFormattingConversionService() {
        var cs = new DefaultFormattingConversionService(false);
        cs.addConverter(String.class, LocalDate.class,s ->
                LocalDate.parse(s, DateTimeFormatter.ofPattern("dd-MM-yyyy")));
        return cs;
    }

    // ---------- WRITERS (JDBC vers STAGING) ----------
    @Bean
    public JdbcBatchItemWriter<StgCustomer> stgCustomerWriter(DataSource ds) {
        var w = new JdbcBatchItemWriter<StgCustomer>();
        w.setDataSource(ds);
        w.setSql("""
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
                """);
        w.setItemSqlParameterSourceProvider(new BeanPropertyItemSqlParameterSourceProvider<>());
        w.afterPropertiesSet();
        return w;
    }


    @Bean
    public JdbcBatchItemWriter<StgOrder> stgOrderWriter(DataSource ds) {
        var w = new JdbcBatchItemWriter<StgOrder>();
        w.setDataSource(ds);
        w.setSql("""
                INSERT INTO stg_order (order_id, customer_id, order_date, amount, status)
                VALUES (:id, :customerId, :orderDate, :amount, :status)
                ON CONFLICT (order_id) DO UPDATE SET
                  customer_id = EXCLUDED.customer_id,
                  order_date  = EXCLUDED.order_date,
                  amount      = EXCLUDED.amount,
                  status      = EXCLUDED.status
                """);
        w.setItemSqlParameterSourceProvider(new BeanPropertyItemSqlParameterSourceProvider<>());
        w.afterPropertiesSet();
        return w;
    }

    // ---------- TASKLET: TRUNCATE STAGING (optionnel si tu préfères “replace” plutôt que upsert) ----------
    @Bean
    public Tasklet truncateStagingTasklet(JdbcTemplate jdbcTemplate) {
        return (contribution, chunkContext) -> {
            jdbcTemplate.update("TRUNCATE TABLE stg_order");
            jdbcTemplate.update("TRUNCATE TABLE stg_customer");
            return RepeatStatus.FINISHED;
        };
    }

    // ---------- STEPS ----------
    @Bean
    public Step truncateStagingStep(JobRepository jr, PlatformTransactionManager tx, Tasklet truncateStagingTasklet) {
        return new StepBuilder("truncate-staging", jr)
                .tasklet(truncateStagingTasklet, tx)
                .build();
    }

    @Bean
    public Step importCustomersStep(JobRepository jr, PlatformTransactionManager tx,
                                    FlatFileItemReader<StgCustomer> customerReader,
                                    JdbcBatchItemWriter<StgCustomer> stgCustomerWriter) {
        return new StepBuilder("import-customers", jr)
                .<StgCustomer, StgCustomer>chunk(1000, tx)
                .reader(customerReader)
                .writer(stgCustomerWriter)
                .build();
    }

    @Bean
    public Step importOrdersStep(JobRepository jr, PlatformTransactionManager tx,
                                 FlatFileItemReader<StgOrder> orderReader,
                                 JdbcBatchItemWriter<StgOrder> stgOrderWriter) {
        return new StepBuilder("import-orders", jr)
                .<StgOrder, StgOrder>chunk(1000, tx)
                .reader(orderReader)
                .writer(stgOrderWriter)
                .build();
    }

    // ---------- JOB ----------
    // @Bean
    // public Job importStagingJob(JobRepository jr,
    //                             Step truncateStagingStep,
    //                             Step importCustomersStep,
    //                             Step importOrdersStep) {
    //     return new JobBuilder("importToStaging", jr)
    //             .start(truncateStagingStep)
    //             .next(importCustomersStep)
    //             .next(importOrdersStep)
    //             .build();
    // }
}

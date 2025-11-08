package com.esprit.config;

import lombok.Builder;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.job.builder.JobBuilder;
import org.springframework.batch.core.job.builder.SimpleJobBuilder;
import org.springframework.batch.core.repository.JobRepository;
import org.springframework.batch.core.step.builder.StepBuilder;
import org.springframework.batch.item.database.JdbcBatchItemWriter;
import org.springframework.batch.item.file.FlatFileItemReader;
import org.springframework.batch.item.file.mapping.DefaultLineMapper;
import org.springframework.batch.item.file.transform.DelimitedLineTokenizer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.FileSystemResource;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.transaction.PlatformTransactionManager;

import javax.sql.DataSource;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

@Configuration
@RequiredArgsConstructor
class DynamicImportJobConfig {

    private final JobRepository jobRepository;
    private final PlatformTransactionManager transactionManager;
    private final DataSource dataSource;

    /**
     * Définition d’un import “générique” :
     * - name         : pour nommer le step (import-<name>)
     * - file         : chemin du CSV (avec header)
     * - columns      : noms des colonnes du CSV (les mêmes que les :params SQL)
     * - sql          : SQL d’upsert vers la table de staging (tu peux y mettre des to_date(...))
     */
    @Data @Builder
    public static class ImportDef {
        private String name;
        private String file;
        private List<String> columns;
        private String sql;
    }

    /**
     * ⚠️ Exemple : ici on déclare 2 imports (customers + orders).
     * En prod, lis ça depuis un YAML (PropertiesBinding) ou une table "batch_import_def".
     */
    private List<ImportDef> importCatalog() {
        List<ImportDef> defs = new ArrayList<>();

        // customers.csv avec dates au format dd-MM-yyyy -> on caste côté SQL
        defs.add(ImportDef.builder()
                .name("customers")
                .file("src/main/resources/customers.csv")
                .columns(List.of("id", "firstName", "lastName", "email", "gender", "contactNo", "country", "dob"))
                .sql("""
                    INSERT INTO stg_customer (customer_id, first_name, last_name, email, gender, contact, country, dob)
                    VALUES (:id::bigint, :firstName, :lastName, :email, :gender, :contactNo, :country, to_date(:dob,'DD-MM-YYYY'))
                    ON CONFLICT (customer_id) DO UPDATE SET
                      first_name = EXCLUDED.first_name,
                      last_name  = EXCLUDED.last_name,
                      email      = EXCLUDED.email,
                      gender     = EXCLUDED.gender,
                      contact    = EXCLUDED.contact,
                      country    = EXCLUDED.country,
                      dob        = EXCLUDED.dob
                    """)
                .build());

        // orders.csv (exemple) – à adapter à ton schéma réel
        defs.add(ImportDef.builder()
                .name("orders")
                .file("src/main/resources/orders.csv")
                .columns(List.of("id", "customerId", "orderDate", "amount", "status"))
                .sql("""
                    INSERT INTO stg_order (order_id, customer_id, order_date, amount, status)
                    VALUES (:id::bigint, :customerId::bigint, to_date(:orderDate,'DD-MM-YYYY'), :amount::numeric, :status)
                    ON CONFLICT (order_id) DO UPDATE SET
                      customer_id = EXCLUDED.customer_id,
                      order_date  = EXCLUDED.order_date,
                      amount      = EXCLUDED.amount,
                      status      = EXCLUDED.status
                    """)

                .build());

        return defs;
    }

    /** Step technique : TRUNCATE des tables staging (optionnel si tu es en upsert-only) */
    @Bean
    public Step truncateDynamicStagingStep() {
        return new StepBuilder("truncate-staging", jobRepository)
                .tasklet((contribution, chunkContext) -> {
                    JdbcTemplate jdbc = new JdbcTemplate(dataSource);
                    // liste toutes tes tables staging ici
                    jdbc.execute("TRUNCATE TABLE stg_order, stg_customer RESTART IDENTITY CASCADE");
                    return org.springframework.batch.repeat.RepeatStatus.FINISHED;
                }, transactionManager)
                .build();
    }

    /** Fabrique un reader générique Map<String,Object> basé sur l’entête du CSV */
    private FlatFileItemReader<Map<String, Object>> buildReader(ImportDef def) {
        FlatFileItemReader<Map<String, Object>> reader = new FlatFileItemReader<>();
        reader.setName("reader-" + def.getName());
        reader.setResource(new FileSystemResource(def.getFile()));
        reader.setLinesToSkip(1);

        DefaultLineMapper<Map<String, Object>> lineMapper = new DefaultLineMapper<>();

        DelimitedLineTokenizer tokenizer = new DelimitedLineTokenizer();
        tokenizer.setDelimiter(DelimitedLineTokenizer.DELIMITER_COMMA);
        tokenizer.setStrict(false);
        tokenizer.setNames(def.getColumns().toArray(new String[0]));

        lineMapper.setLineTokenizer(tokenizer);
        lineMapper.setFieldSetMapper(fs -> {
            Map<String, Object> map = new LinkedHashMap<>();
            for (String col : def.getColumns()) {
                map.put(col, fs.readString(col)); // keep as String; cast in SQL (to_date, ::numeric, etc.)
            }
            return map;
        });

        reader.setLineMapper(lineMapper);
        return reader;
    }

    /** petit util pour includedFields */
    private int[] range(int size) {
        int[] r = new int[size];
        for (int i = 0; i < size; i++) r[i] = i;
        return r;
    }

    /** Fabrique un writer JDBC générique (Map -> Named params) */
    private JdbcBatchItemWriter<Map<String, Object>> buildWriter(ImportDef def) {
        JdbcBatchItemWriter<Map<String, Object>> w = new JdbcBatchItemWriter<>();
        w.setDataSource(dataSource);
        w.setSql(def.getSql());
        // Map<String,Object> -> MapSqlParameterSource
        w.setItemSqlParameterSourceProvider(item -> new MapSqlParameterSource(item));
        w.afterPropertiesSet();
        return w;
    }

    /** Construit un Step d’import (reader/writer) pour une définition */
    private Step buildImportStep(ImportDef def) {
        var reader = buildReader(def);
        var writer = buildWriter(def);

        // IMPORTANT : ne pas ouvrir le reader ici (Batch s’en charge) – pas de reader.open(...)

        return new StepBuilder("import-" + def.getName(), jobRepository)
                .<Map<String, Object>, Map<String, Object>>chunk(1000, transactionManager)
                .reader(reader)
                .writer(writer)
                .faultTolerant()
                .skipLimit(100) // à ajuster (format KO, etc.)
                .skip(Exception.class)
                .build();
    }

    /** Construit une liste de Steps à partir du catalogue */
    private List<Step> buildAllImportSteps() {
        return importCatalog().stream()
                .map(this::buildImportStep)
                .toList();
    }

    /** Job linéaire : truncate -> step1 -> step2 -> ... -> stepN */

    /*
    @Bean
    public Job importDynamicStagingJob() {
        List<Step> steps = buildAllImportSteps();

        JobBuilder jb = new JobBuilder("importToStaging", jobRepository);

        if (steps.isEmpty()) {
            // only truncate if nothing to import
            return jb.start(truncateDynamicStagingStep()).build();
        }

        // start with truncate, then chain each import step
        SimpleJobBuilder sjb = jb.start(truncateDynamicStagingStep());
        for (Step s : steps) {
            sjb = sjb.next(s);
        }
        return sjb.build();
    }

     */
}

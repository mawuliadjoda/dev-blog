package com.esprit.batch.importstg;

import com.esprit.config.config.BatchIdEnricherProcessor;
import lombok.RequiredArgsConstructor;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.repository.JobRepository;
import org.springframework.batch.core.step.builder.StepBuilder;
import org.springframework.batch.item.database.BeanPropertyItemSqlParameterSourceProvider;
import org.springframework.batch.item.database.JdbcBatchItemWriter;
import org.springframework.batch.item.file.FlatFileItemReader;
import org.springframework.batch.item.file.mapping.DefaultLineMapper;
import org.springframework.batch.item.file.transform.DelimitedLineTokenizer;
import org.springframework.batch.repeat.RepeatStatus;
import org.springframework.core.io.FileSystemResource;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;
import org.springframework.transaction.PlatformTransactionManager;

import javax.sql.DataSource;

// importstg/ImportBuilders.java
@Component
@RequiredArgsConstructor
public class ImportBuilders {
    private final JobRepository jobRepository;
    private final PlatformTransactionManager tx;
    private final DataSource dataSource;

    public <T> FlatFileItemReader<T> reader(ImportCatalog.ImportDef<T> def) {
        var r = new FlatFileItemReader<T>();
        r.setName("reader-" + def.name());
        r.setResource(new FileSystemResource(def.file()));
        r.setLinesToSkip(1);
        var lm = new DefaultLineMapper<T>();
        var tok = new DelimitedLineTokenizer();
        tok.setDelimiter(",");
        tok.setStrict(false);
        tok.setNames(def.columns());
        lm.setLineTokenizer(tok);
        lm.setFieldSetMapper(fs -> def.mapper().apply(fs));
        r.setLineMapper(lm);
        return r;
    }

    public <T> JdbcBatchItemWriter<T> writer(String upsertSql) {
        var w = new JdbcBatchItemWriter<T>();
        w.setDataSource(dataSource);
        w.setSql(upsertSql);
        w.setItemSqlParameterSourceProvider(new BeanPropertyItemSqlParameterSourceProvider<>());
        w.afterPropertiesSet();
        return w;
    }

    public <T> Step importStep(ImportCatalog.ImportDef<T> def) {
        return new StepBuilder("import-" + def.name(), jobRepository)
                .<T,T>chunk(1000, tx)
                .reader(reader(def))
                .processor(new BatchIdEnricherProcessor<>())
                .writer(writer(def.upsertSql()))
                .build();
    }

    public Step truncateStagingStep() {
        return new StepBuilder("truncate-staging", jobRepository)
                .tasklet((c,ctx)->{
                    new JdbcTemplate(dataSource)
                            .execute("TRUNCATE TABLE stg_order, stg_customer RESTART IDENTITY CASCADE");
                    return RepeatStatus.FINISHED;
                }, tx)
                .build();
    }
}

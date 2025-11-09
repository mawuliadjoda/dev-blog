package com.esprit.batch.target;

import lombok.RequiredArgsConstructor;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.repository.JobRepository;
import org.springframework.batch.core.step.builder.StepBuilder;
import org.springframework.batch.repeat.RepeatStatus;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.ClassPathResource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.transaction.PlatformTransactionManager;

import java.nio.charset.StandardCharsets;
import java.util.Map;

// target/InsertTargetsConfig.java
@Configuration
@RequiredArgsConstructor
public class InsertTargetsConfig {
    private final JobRepository jobRepository;
    private final PlatformTransactionManager tx;
    private final NamedParameterJdbcTemplate jdbc;

    private String sql(String path) {
        try (var in = new ClassPathResource(path).getInputStream()) {
            return new String(in.readAllBytes(), StandardCharsets.UTF_8);
        } catch (Exception e) {
            throw new IllegalStateException("Cannot load SQL: " + path, e);
        }
    }

    @Bean
    public Step insertTargetsStep() {
        return new StepBuilder("insertTargetsStep", jobRepository)
                .tasklet((contrib, ctx) -> {
                    var batchId = String.valueOf(ctx.getStepContext().getJobParameters().get("batchId"));
                    var p = Map.of("batchId", batchId);

                    int n1 = jdbc.update(sql("batch/target-sql/10_upsert_customer.sql"), p);
                    int n2 = jdbc.update(sql("batch/target-sql/20_upsert_orders.sql"), p);
                    contrib.incrementWriteCount(n1 + n2);
                    return RepeatStatus.FINISHED;
                }, tx)
                .build();
    }
}

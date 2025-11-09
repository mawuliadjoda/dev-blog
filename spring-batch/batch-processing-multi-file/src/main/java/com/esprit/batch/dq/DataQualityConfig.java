package com.esprit.batch.dq;

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

// dq/DataQualityConfig.java
@Configuration
@RequiredArgsConstructor
public class DataQualityConfig {
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
    public Step cleanAndRejectStep() {
        return new StepBuilder("cleanAndRejectStep", jobRepository)
                .tasklet((contrib, ctx) -> {
                    var batchId = String.valueOf(ctx.getStepContext().getJobParameters().get("batchId"));
                    var p = Map.of("batchId", batchId);

                    // reset
                    jdbc.update(sql("batch/dq-sql/00_reset.sql"), p);
                    // customer rules
                    jdbc.update(sql("batch/dq-sql/10_customer_email.sql"), p);
                    jdbc.update(sql("batch/dq-sql/11_customer_gender.sql"), p);
                    jdbc.update(sql("batch/dq-sql/12_customer_dob_future.sql"), p);
                    jdbc.update(sql("batch/dq-sql/13_customer_age_range.sql"), p);
                    // order rules
                    jdbc.update(sql("batch/dq-sql/20_order_amount.sql"), p);
                    jdbc.update(sql("batch/dq-sql/21_order_status.sql"), p);
                    jdbc.update(sql("batch/dq-sql/22_order_date_future.sql"), p);
                    jdbc.update(sql("batch/dq-sql/30_order_fk_customer.sql"), p);
                    // rejets
                    jdbc.update(sql("batch/dq-sql/90_rejets_purge.sql"), p);
                    jdbc.update(sql("batch/dq-sql/91_rejets_from_customer.sql"), p);
                    jdbc.update(sql("batch/dq-sql/92_rejets_from_order.sql"), p);

                    return RepeatStatus.FINISHED;
                }, tx)
                .build();
    }
}

package com.esprit.batch.step;

import com.esprit.persistence.repository.StgCustomerRepository;
import com.esprit.persistence.repository.StgOrderRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.repository.JobRepository;
import org.springframework.batch.core.step.builder.StepBuilder;
import org.springframework.batch.repeat.RepeatStatus;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.transaction.PlatformTransactionManager;

@Configuration
@RequiredArgsConstructor
public class TruncateStagingConfig {

    private final JobRepository jobRepository;
    private final PlatformTransactionManager tx;

    private final StgCustomerRepository stgCustomerRepository;
    private final StgOrderRepository stgOrderRepository;

    @Bean
    public Step truncateStagingStepJpa() {
        return new StepBuilder("truncate-staging-jpa", jobRepository)
                .tasklet((contribution, chunkCtx) -> {

                    // ⚠ IMPORTANT : TRUNCATE ne peut PAS être fait via JPA DELETE dans une vraie app.
                    // Mais pour ton exemple JPA :
                    stgOrderRepository.truncate();
                    stgCustomerRepository.truncate();

                    // Si tu veux faire un TRUNCATE SQL réel :
                    // new JdbcTemplate(dataSource).execute("TRUNCATE TABLE stg_order, stg_customer RESTART IDENTITY");

                    return RepeatStatus.FINISHED;
                }, tx)
                .build();
    }
}

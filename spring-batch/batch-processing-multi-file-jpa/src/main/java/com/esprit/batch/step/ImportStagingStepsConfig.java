package com.esprit.batch.step;

import com.esprit.batch.processor.BatchIdEnricherProcessor;
import com.esprit.batch.reader.ImportReadersConfig;
import com.esprit.persistence.entities.StgCustomerEntity;
import com.esprit.persistence.entities.StgOrderEntity;
import com.esprit.persistence.repository.StgCustomerRepository;
import com.esprit.persistence.repository.StgOrderRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.repository.JobRepository;
import org.springframework.batch.core.step.builder.StepBuilder;
import org.springframework.batch.item.data.RepositoryItemWriter;
import org.springframework.batch.repeat.RepeatStatus;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.transaction.PlatformTransactionManager;

@Configuration
@RequiredArgsConstructor
public class ImportStagingStepsConfig {

    private final JobRepository jobRepository;
    private final PlatformTransactionManager tx;

    private final ImportReadersConfig importReadersConfig;          // <-- nouveaux readers
    private final RepositoryItemWriter<StgCustomerEntity> stgCustomerWriter;
    private final RepositoryItemWriter<StgOrderEntity> stgOrderWriter;

    private final StgCustomerRepository stgCustomerRepository;
    private final StgOrderRepository stgOrderRepository;

    /**
     * Step qui vide les tables de staging.
     * Ici on passe par les repositories JPA (delete all),
     * mais tu peux aussi utiliser JdbcTemplate(TRUNCATE ...) si tu préfères.
     */
    @Bean
    public Step truncateStagingStepJpa() {
        return new StepBuilder("truncate-staging-jpa", jobRepository)
                .tasklet((contribution, chunkCtx) -> {
                    stgOrderRepository.truncate();
                    stgCustomerRepository.truncate();
                    return RepeatStatus.FINISHED;
                }, tx)
                .build();
    }

    /**
     * Step d'import des clients depuis le CSV vers stg_customer via JPA.
     */
    @Bean
    public Step importCustomersStep() {
        return new StepBuilder("import-customers", jobRepository)
                .<StgCustomerEntity, StgCustomerEntity>chunk(1000, tx)
                .reader(importReadersConfig.stgCustomerReader())
                .processor(new BatchIdEnricherProcessor<>())
                .writer(stgCustomerWriter)
                .build();
    }

    /**
     * Step d'import des commandes depuis le CSV vers stg_order via JPA.
     */
    @Bean
    public Step importOrdersStep() {
        return new StepBuilder("import-orders", jobRepository)
                .<StgOrderEntity, StgOrderEntity>chunk(1000, tx)
                .reader(importReadersConfig.stgOrderReader())
                .processor(new BatchIdEnricherProcessor<>())
                .writer(stgOrderWriter)
                .build();
    }
}


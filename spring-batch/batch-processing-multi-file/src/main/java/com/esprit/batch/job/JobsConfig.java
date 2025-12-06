package com.esprit.batch.job;

import lombok.RequiredArgsConstructor;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.job.builder.FlowBuilder;
import org.springframework.batch.core.job.builder.JobBuilder;
import org.springframework.batch.core.job.flow.Flow;
import org.springframework.batch.core.job.flow.JobExecutionDecider;
import org.springframework.batch.core.repository.JobRepository;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

// job/JobsConfig.java
@Configuration
@RequiredArgsConstructor
public class JobsConfig {
    private final JobRepository jobRepository;
    private final Flow importStagingFlow;
    private final Step cleanAndRejectStep;
    private final Step insertTargetsStep;
    private final Step downloadStep;
    private final Step deleteDownloadStep;



    @Bean
    public Job importToStagingJob() {
        return new JobBuilder("importToStaging", jobRepository)
                .start(importStagingFlow)
                .end()
                .build();
    }

    @Bean
    public Job importThenCleanJob() {
        return new JobBuilder("importThenClean", jobRepository)
                .start(importStagingFlow)
                .next(cleanAndRejectStep)
                .end()
                .build();
    }

    @Bean
    public Job importCleanInsertJob() {
        return new JobBuilder("importCleanInsert", jobRepository)
                .start(importStagingFlow)
                .next(cleanAndRejectStep)
                .next(insertTargetsStep)
                .end()
                .build();
    }

    @Bean
    public Job downloadimportCleanInsertJob() {
        Flow mainFlow = new FlowBuilder<Flow>("downloadImportCleanInsertFlow")
                .start(downloadStep)        // Step (tasklet) -> OK dans un Flow
                .next(importStagingFlow)    // Flow
                .next(cleanAndRejectStep)   // Step
                .next(insertTargetsStep)    // Step
                .next(deleteDownloadStep)
                .end();

        return new JobBuilder("downloadimportCleanInsertJob", jobRepository)
                .start(mainFlow)            // on démarre le job avec le Flow
                .end()
                .build();
    }
}

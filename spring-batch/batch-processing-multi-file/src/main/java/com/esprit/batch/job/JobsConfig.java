package com.esprit.batch.job;

import lombok.RequiredArgsConstructor;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.job.builder.JobBuilder;
import org.springframework.batch.core.job.flow.Flow;
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
}

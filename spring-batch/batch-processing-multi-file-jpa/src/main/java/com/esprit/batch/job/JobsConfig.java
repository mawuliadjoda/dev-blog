package com.esprit.batch.job;

import lombok.RequiredArgsConstructor;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.job.builder.JobBuilder;
import org.springframework.batch.core.job.flow.Flow;
import org.springframework.batch.core.repository.JobRepository;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@RequiredArgsConstructor
public class JobsConfig {

    private final Flow importStagingFlowJpa;

    @Bean
    public Job importToStagingJob(JobRepository jobRepository) {
        return new JobBuilder("importToStagingJob", jobRepository)
                .start(importStagingFlowJpa)
                .end()
                .build();
    }
}

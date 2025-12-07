package com.esprit.batch.step.config.parallel;

import com.esprit.batch.reader.ImportCatalog;
import com.esprit.batch.step.ImportStagingStepsConfig;
import lombok.RequiredArgsConstructor;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.job.builder.FlowBuilder;
import org.springframework.batch.core.job.flow.Flow;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.task.TaskExecutor;

@Configuration
@RequiredArgsConstructor
public class ImportStagingFlowConfig {

    private final Step truncateStagingStepJpa;


    private final ImportStagingStepsConfig  importStagingStepsConfig;
    private final TaskExecutor stepTaskExecutor;
    private final ImportCatalog catalog;

    @Bean
    public Flow importStagingFlowJpa() {
        var steps = catalog.catalog().stream()
                .map(importStagingStepsConfig::importStep)
                .toList();

        var flows = steps.stream()
                .map(s -> new FlowBuilder<Flow>(s.getName() + "Flow").start(s).end())
                .toList();

        return new FlowBuilder<Flow>("importStagingFlowJpa")
                .start(truncateStagingStepJpa)
                .split(stepTaskExecutor)
                .add(flows.toArray(new Flow[0]))
                .build();
    }


}

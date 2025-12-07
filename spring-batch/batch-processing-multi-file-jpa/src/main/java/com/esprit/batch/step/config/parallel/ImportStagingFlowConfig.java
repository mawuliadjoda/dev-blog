package com.esprit.batch.step.config.parallel;

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
    private final Step importCustomersStep;
    private final Step importOrdersStep;
    private final TaskExecutor stepTaskExecutor;

    @Bean
    public Flow customersImportFlow() {
        return new FlowBuilder<Flow>("customersImportFlow")
                .start(importCustomersStep)
                .end();
    }

    @Bean
    public Flow ordersImportFlow() {
        return new FlowBuilder<Flow>("ordersImportFlow")
                .start(importOrdersStep)
                .end();
    }

    @Bean
    public Flow importStagingFlowJpa() {
        return new FlowBuilder<Flow>("importStagingFlowJpa")
                .start(truncateStagingStepJpa)
                .next(
                        new FlowBuilder<Flow>("parallelImports")
                                .split(stepTaskExecutor)
                                .add(customersImportFlow(), ordersImportFlow())
                                .build()
                )
                .end();
    }


}

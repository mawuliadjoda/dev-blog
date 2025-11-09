package com.esprit.batch.importstg;

import lombok.RequiredArgsConstructor;
import org.springframework.batch.core.job.builder.FlowBuilder;
import org.springframework.batch.core.job.flow.Flow;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.task.TaskExecutor;

@Configuration
@RequiredArgsConstructor
public class ImportFlowConfig {
    private final ImportCatalog catalog;
    private final ImportBuilders builders;
    private final TaskExecutor stepTaskExecutor;

    @Bean
    public Flow importStagingFlow() {
        var steps = catalog.catalog().stream().map(builders::importStep).toList();
        var flows = steps.stream()
                .map(s -> new FlowBuilder<Flow>(s.getName() + "Flow").start(s).end())
                .toList();

        return new FlowBuilder<Flow>("importStagingFlow")
                .start(builders.truncateStagingStep())
                .split(stepTaskExecutor)
                .add(flows.toArray(new Flow[0]))
                .build();
    }
}
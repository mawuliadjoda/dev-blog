package com.esprit.batch.step.config.multithread;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.task.SimpleAsyncTaskExecutor;
import org.springframework.core.task.TaskExecutor;

@Configuration
public class BatchExecutorConfig {

    @Bean
    public TaskExecutor stepTaskExecutor() {
        // Pour le POC, SimpleAsyncTaskExecutor.
        // En prod : plutôt un ThreadPoolTaskExecutor configuré.
        var executor = new SimpleAsyncTaskExecutor("step-");
        executor.setConcurrencyLimit(4); // limite de threads concurrents
        return executor;
    }
}


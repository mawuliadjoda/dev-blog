package com.esprit.batch.infra;

import com.esprit.batch.importstg.ImportProperties;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.task.TaskExecutor;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

import javax.sql.DataSource;

@Configuration
@EnableConfigurationProperties(ImportProperties.class)
public class InfrastructureConfig {

    @Bean
    public NamedParameterJdbcTemplate namedParameterJdbcTemplate(DataSource ds) {
        return new NamedParameterJdbcTemplate(ds);
    }

    @Bean
    public TaskExecutor stepTaskExecutor() {
        var t = new ThreadPoolTaskExecutor();
        t.setThreadNamePrefix("batch-");
        t.setCorePoolSize(4);
        t.setMaxPoolSize(4);
        t.setQueueCapacity(0);
        t.initialize();
        return t;
    }
}
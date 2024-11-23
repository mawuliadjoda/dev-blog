package com.esprit.library;


import javax.sql.DataSource;
import liquibase.integration.spring.SpringLiquibase;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class LibraryLiquibaseConfig {


    @Bean
    public SpringLiquibase liquibase(LibraryLiquibaseProperties properties, DataSource dataSource) {
        SpringLiquibase liquibase = new SpringLiquibase();
        liquibase.setDataSource(dataSource);
        liquibase.setChangeLog(properties.getChangelogPath());
        liquibase.setShouldRun(true);
        return liquibase;
    }
}

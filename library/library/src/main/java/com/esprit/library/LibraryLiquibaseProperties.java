package com.esprit.library;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Configuration
@ConfigurationProperties(prefix = "library.liquibase")
public class LibraryLiquibaseProperties {
    private String changelogPath;

    public String getChangelogPath() {
        return changelogPath;
    }

    public void setChangelogPath(String changelogPath) {
        this.changelogPath = changelogPath;
    }
}

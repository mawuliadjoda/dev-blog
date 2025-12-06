package com.esprit.batch.downloadfile;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;

@Setter
@Getter
@ConfigurationProperties(prefix = "file.download")
public class FileStorageProperties {
    private String directory;
}
package com.esprit.batch.downloadfile.properties;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;


@Setter
@Getter
@ConfigurationProperties(prefix = "file.provider")
public class FileProviderProperties {
    private String baseUrl;
}
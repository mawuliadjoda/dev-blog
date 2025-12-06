package com.esprit.batch.downloadfile.restclient;

import com.esprit.batch.downloadfile.properties.FileProviderProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestClient;

@Configuration
public class RestClientConfig {

    @Bean
    public RestClient restClient(FileProviderProperties props) {
        return RestClient.builder()
                .baseUrl(props.getBaseUrl())
                .build();
    }
}
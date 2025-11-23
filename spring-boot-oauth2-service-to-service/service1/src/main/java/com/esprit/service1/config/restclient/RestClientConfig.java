package com.esprit.service1.config.restclient;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.oauth2.client.OAuth2AuthorizedClientManager;
import org.springframework.web.client.RestClient;

@Configuration
@RequiredArgsConstructor
public class RestClientConfig {

    private final OAuth2AuthorizedClientManager authorizedClientManager;

    @Bean
    RestClient service2RestClient(@Value("${service-2.base-url}") String baseUrl) {

        var interceptor = new OAuth2ClientCredentialsInterceptor(authorizedClientManager, "service-1-to-2-registrationId");

        return RestClient.builder()
                .baseUrl(baseUrl)
                .requestInterceptor(interceptor)
                .build();
    }
}


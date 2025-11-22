package com.esprit.service1.config;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.oauth2.client.OAuth2AuthorizedClientManager;
import org.springframework.security.oauth2.client.web.reactive.function.client.ServletOAuth2AuthorizedClientExchangeFilterFunction;
import org.springframework.web.reactive.function.client.WebClient;

@Configuration
@RequiredArgsConstructor
public class WebClientConfig {

    private final OAuth2AuthorizedClientManager authorizedClientManager;

    @Bean
    WebClient service2WebClient(@Value("${service-2.base-url}") String baseUrl) {
        var oauth2 = new ServletOAuth2AuthorizedClientExchangeFilterFunction(authorizedClientManager);
        oauth2.setDefaultClientRegistrationId("service-1-to-2-registrationId"); // celui défini dans application.yml

        return WebClient.builder()
                .baseUrl(baseUrl)
                .apply(oauth2.oauth2Configuration())
                .build();
    }

    @Bean
    WebClient service3WebClient(@Value("${service-3.base-url}") String baseUrl) {
        var oauth2 = new ServletOAuth2AuthorizedClientExchangeFilterFunction(authorizedClientManager);
        oauth2.setDefaultClientRegistrationId("service-1-to-3-registrationId"); // celui défini dans application.yml

        return WebClient.builder()
                .baseUrl(baseUrl)
                .apply(oauth2.oauth2Configuration())
                .build();
    }
}

package com.esprit.service1.service;

import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClient;

@Service
@RequiredArgsConstructor
public class Service2Client {

    // We can either use WebClient or RestClient

    // private final WebClient service2WebClient;
    private final RestClient service2RestClient;

    public String callService2HelloFromService1() {

        /*
        return service2WebClient.get()
                .uri("/api/hello")
                .retrieve()
                .bodyToMono(String.class)
                .block();

         */

        return service2RestClient.get()
                .uri("/api/hello")
                .accept(MediaType.APPLICATION_JSON)
                .retrieve()
                .body(String.class);
    }
}


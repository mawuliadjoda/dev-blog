package com.esprit.service1.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

@Service
@RequiredArgsConstructor
public class Service2Client {

    private final WebClient service2WebClient;

    public String callService2HelloFromService1() {
        return service2WebClient.get()
                .uri("/api/hello")
                .retrieve()
                .bodyToMono(String.class)
                .block();
    }
}


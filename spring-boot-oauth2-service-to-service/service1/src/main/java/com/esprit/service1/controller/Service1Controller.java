package com.esprit.service1.controller;

import com.esprit.service1.service.Service2Client;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class Service1Controller {

    private final Service2Client service2Client;

    @GetMapping("/from-a")
    public String from1(@AuthenticationPrincipal Jwt jwt) {
        String responseFromB = service2Client.callService2HelloFromService1();
        return "A (sub=" + jwt.getSubject() + ") → " + responseFromB;
    }
}

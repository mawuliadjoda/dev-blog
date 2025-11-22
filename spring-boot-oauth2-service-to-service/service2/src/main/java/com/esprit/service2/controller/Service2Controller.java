package com.esprit.service2.controller;


import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api")
public class Service2Controller {

    @PreAuthorize("hasAnyAuthority('SCOPE_service2:resource1', 'SCOPE_service2:resource2')")
    @GetMapping("/hello")
    public String hello(@AuthenticationPrincipal Jwt jwt) {
        return "Hello from B, sub=" + jwt.getSubject();
    }
}

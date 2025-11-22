package com.esprit.service1.controller;


import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

@RestController
@CrossOrigin(
        origins = "*",
        allowedHeaders = "*",
        methods = {RequestMethod.GET}
)
@RequestMapping("/api/v1")
public class HelloController {

    @GetMapping("/hello")
    public Message hello() {
        var jwt = SecurityContextHolder.getContext().getAuthentication();

        var isAuthenticated = jwt != null && jwt.isAuthenticated();

        return new Message("Hello fullstack master, you are authenticated: {1} " + isAuthenticated);
    }

    record Message(String message) {
    }

    ;

}

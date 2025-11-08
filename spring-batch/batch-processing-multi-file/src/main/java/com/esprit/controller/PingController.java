package com.esprit.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1")
@RequiredArgsConstructor
public class PingController {

    @GetMapping("/ping")
    ResponseEntity<MyReccord> ping() {
        return new ResponseEntity<>(new MyReccord("Hello spring batch"), HttpStatus.OK);
    }

    record MyReccord(String name) {
    }
}

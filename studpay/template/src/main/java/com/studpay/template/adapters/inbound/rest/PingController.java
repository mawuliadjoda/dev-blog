package com.studpay.template.adapters.inbound.rest;


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
        return new ResponseEntity<>(new MyReccord("Hello helm"), HttpStatus.OK);
    }


    @GetMapping("/ping2")
    ResponseEntity<MyReccord> ping2() {
        return new ResponseEntity<>(new MyReccord("Hello helm"), HttpStatus.OK);
    }

    record MyReccord(String name){}
}

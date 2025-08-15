package com.esprit.order.adapters.inbound.rest;


import com.esprit.security.security.jwt.CustomJwt;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.text.MessageFormat;

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


    @GetMapping("/hello")
    // @PreAuthorize("hasAnyRole('ROLE_USER', 'ROLE_ADMIN')")
    //@PreAuthorize("hasAuthority('CAN_ASSIGN_CONTRACTS')")
    @PreAuthorize("hasAnyAuthority('CAN_ASSIGN_CONTRACTS', 'CAN_VIEW_CLIENTS')")
    public MyReccord hello() {
        var jwt = (CustomJwt) SecurityContextHolder.getContext().getAuthentication();
        var message = MessageFormat.format(
                "Hello fullstack master {0} {1}, how is it going today ?",
                jwt.getFirstname(), jwt.getLastname()
        );
        return new MyReccord(message);
    }
    record MyReccord(String name){}
}

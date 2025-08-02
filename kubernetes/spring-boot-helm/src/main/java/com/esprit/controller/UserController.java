package com.esprit.controller;


import com.esprit.entities.User;
import com.esprit.repositories.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/v1/users")
public class UserController {

    private final UserRepository userRepository;

    @GetMapping
    ResponseEntity<List<User>> users() {
        return new ResponseEntity<>(userRepository.findAll(), HttpStatus.OK);
    }
}

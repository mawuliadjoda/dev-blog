package com.esprit.consumer.consumer;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@Slf4j
public class MyService {
    public List<String> messages = new ArrayList<>();

    public void processMessage(String message) {
        messages.add(message);
        log.info("Received message: {}", message);
    }
}

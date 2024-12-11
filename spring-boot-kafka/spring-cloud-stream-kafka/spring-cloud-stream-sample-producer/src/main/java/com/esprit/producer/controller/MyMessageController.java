package com.esprit.producer.controller;

import com.esprit.producer.producer.MyMessageProducer;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cloud.stream.function.StreamBridge;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequiredArgsConstructor
public class MyMessageController {
    private final MyMessageProducer myMessageProducer;


    @GetMapping("/send")
    public void sendMessage(@RequestParam(required = false, defaultValue = "test") String data) {
        myMessageProducer.sendMessage(data);
    }
}

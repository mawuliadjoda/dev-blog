package com.esprit.producer.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cloud.stream.function.StreamBridge;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequiredArgsConstructor
public class MyMessageController {
    private final StreamBridge streamBridge;


    @GetMapping("/send")
    public void sendMessage() {
        log.info("producing message :");
        streamBridge.send("myProducer-out-0", "Hello Kafka Stream streamBridge");
    }
}

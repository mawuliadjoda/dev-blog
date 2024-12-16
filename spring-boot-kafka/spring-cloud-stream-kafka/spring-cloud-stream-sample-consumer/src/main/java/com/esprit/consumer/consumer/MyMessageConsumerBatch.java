package com.esprit.consumer.consumer;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.messaging.Message;

import java.util.List;
import java.util.function.Consumer;

@Slf4j
@RequiredArgsConstructor
@Configuration
public class MyMessageConsumerBatch {

    private final MyService myService;


    @Bean
    public Consumer<Message<List<String>>> myConsumerBatch() {
        return message -> {
            log.info("Received message - batch: {}", message.getPayload());
            List<String> payload = message.getPayload();

            log.info("payload={}", payload);
            log.info("Message={}", message);
            message.getPayload().forEach(event -> {
                log.info("event = {}", event);
                myService.processMessage(event);
            });
            message.getHeaders().entrySet().forEach(header -> {
                log.info("header = {}", header);
            });

        };
    }
}

package com.esprit.consumer.consumer;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.messaging.Message;

import java.util.function.Consumer;

@Slf4j
@RequiredArgsConstructor
@Configuration
public class MyMessageConsumer {

   private final MyService myService;

    @Bean
   public Consumer<Message<String>> myConsumer() {
        return message -> {
            log.info("Received message: {}", message.getPayload());
            myService.processMessage(message.getPayload());
        };
    }
}

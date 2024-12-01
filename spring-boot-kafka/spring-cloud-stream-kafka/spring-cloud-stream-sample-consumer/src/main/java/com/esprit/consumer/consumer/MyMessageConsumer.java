package com.esprit.consumer.consumer;

import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;
import org.springframework.messaging.Message;
import org.springframework.stereotype.Component;

import java.util.function.Consumer;

@Slf4j
@Component
public class MyMessageConsumer {

    @Bean
   public Consumer<Message<String>> myConsumer() {
        return message -> {
            log.info("Received message: {}", message.getPayload());
        };
    }
}

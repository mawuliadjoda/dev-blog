package com.esprit.notification.config;

import com.esprit.notification.domain.model.Greeting;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.function.Consumer;

@Configuration
public class KafkaStreamConfig {

    @Bean
    public Consumer<Greeting> consumeMessage() {
        return greeting -> System.out.println("Consumed: " + greeting.getMessage() + " from " + greeting.getSender());
    }
}
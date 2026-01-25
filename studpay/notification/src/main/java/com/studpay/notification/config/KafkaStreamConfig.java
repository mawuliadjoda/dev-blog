package com.esprit.notification.config;

import com.esprit.notification.domain.model.Greeting;
import com.esprit.notification.domain.model.ProductUpdatedEvent;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.function.Consumer;

@Slf4j
@Configuration
public class KafkaStreamConfig {

    @Bean
    public Consumer<Greeting> consumeMessage() {
        return greeting -> System.out.println("Consumed: " + greeting.getMessage() + " from " + greeting.getSender());
    }

    @Bean
    public Consumer<ProductUpdatedEvent> consumeProductMessage() {
        return event -> System.out.println("Product ID: " + event.getProductId() + " | Product Name " + event.getName());
    }
}
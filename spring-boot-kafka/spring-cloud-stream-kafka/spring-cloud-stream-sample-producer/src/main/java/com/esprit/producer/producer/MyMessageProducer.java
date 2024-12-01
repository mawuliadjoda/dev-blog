package com.esprit.producer.producer;

import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;
import org.springframework.messaging.Message;
import org.springframework.messaging.support.MessageBuilder;
import org.springframework.stereotype.Component;

import java.util.function.Supplier;

// @Slf4j
// @Component
public class MyMessageProducer {

    /**
     * Spring Cloud Stream traite les beans Supplier comme des flux de données reactifs. Par défaut,
     * il utilise une polling mechanism qui appelle continuellement le Supplier pour produire des messages,
     * sauf si une configuration explicite limite ce comportement.
     *
     * ==> Solutions pour contrôler la production en passant par un controller par exemple
     */
    //@Bean
    //public Supplier<Message<String>> myProducer() {
    //    return () -> {
    //        String message = "Hello Kafka Stream";
    //        log.info("Sending message: {}", message);
    //        return MessageBuilder.withPayload(message).build();
    //    };
    //}
}

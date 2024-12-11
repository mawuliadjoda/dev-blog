package com.esprit.producer;

import com.esprit.producer.producer.MyMessageProducer;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.cloud.stream.binder.test.OutputDestination;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.messaging.Message;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import static org.assertj.core.api.Assertions.assertThat;

//@ExtendWith(SpringExtension.class)
//@SpringBootTest
class MessageProducerTest {

    @Autowired
    private MyMessageProducer myMessageProducer;

    @Autowired
    private OutputDestination outputDestination;

    //@Test
    void testSendMessage() {
        String testMessage = "Hello from Producer!";
        myMessageProducer.sendMessage(testMessage);

        // Vérification que le message a été envoyé dans le bon topic Kafka
        Message<byte[]> receivedMessage = outputDestination.receive(1000, "myProducer-out-0");
        assertThat(receivedMessage).isNotNull();
        assertThat(new String(receivedMessage.getPayload())).isEqualTo(testMessage);
    }

    @Configuration
    static class TestConfig {

        @Bean
        public OutputDestination outputDestination() {
            return new OutputDestination();
        }
    }
}

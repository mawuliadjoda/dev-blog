package com.esprit.producer.producer;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.cloud.stream.binder.test.InputDestination;
import org.springframework.cloud.stream.binder.test.OutputDestination;
import org.springframework.cloud.stream.binder.test.TestChannelBinderConfiguration;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.messaging.Message;
import org.springframework.messaging.support.GenericMessage;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
class MyMessageProducerTest {

    @Autowired
    private MyMessageProducer myMessageProducer;

    @Autowired
    private InputDestination inputDestination;

    @Autowired
    private OutputDestination outputDestination;

    @Test
    public void testEmptyConfiguration() {
        this.inputDestination.send(new GenericMessage<byte[]>("hello".getBytes()));
        assertThat(outputDestination.receive().getPayload()).isEqualTo("HELLO".getBytes());
    }

    @Test
    void testSendMessage() {
        String testMessage = "Hello from Producer!";
        myMessageProducer.sendMessage(testMessage);  // Envoie du message

        // Vérification que le message a bien été envoyé et reçu
        Message<byte[]> receivedMessage = outputDestination.receive(50000, "myProducer-out-0");
        assertThat(receivedMessage).isNotNull();
        assertThat(new String(receivedMessage.getPayload())).isEqualTo(testMessage);

    }
}

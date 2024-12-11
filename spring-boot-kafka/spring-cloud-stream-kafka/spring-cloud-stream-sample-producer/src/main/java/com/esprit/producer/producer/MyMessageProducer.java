package com.esprit.producer.producer;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.cloud.stream.function.StreamBridge;
import org.springframework.messaging.Message;
import org.springframework.messaging.support.MessageBuilder;
import org.springframework.stereotype.Component;

//@EnableAutoConfiguration
@Component
public class MyMessageProducer {

    private final StreamBridge streamBridge;

    public MyMessageProducer(StreamBridge streamBridge) {
        this.streamBridge = streamBridge;
    }

    public void sendMessage(String messageContent) {
        Message<String> message = MessageBuilder.withPayload(messageContent).build();
        streamBridge.send("myProducer-out-0", message);  // Envoie du message via StreamBridge
    }
}

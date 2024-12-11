package com.esprit.consumer.consumer;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.ImportAutoConfiguration;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.cloud.stream.binder.test.InputDestination;
import org.springframework.cloud.stream.binder.test.TestChannelBinderConfiguration;
import org.springframework.integration.support.MessageBuilder;

import static org.junit.jupiter.api.Assertions.assertEquals;


@ImportAutoConfiguration(TestChannelBinderConfiguration.class)
@SpringBootTest(properties = {"spring.cloud.function.definition=myConsumer"})
class MyMessageConsumerTest {

    @Value("${spring.cloud.stream.bindings.myConsumer-in-0.destination}")
    private String inputTopic;

    @Autowired
    private InputDestination inputDestination;

    @Autowired
    private MyService myService;

    @Test
    void shouldConsumeMessage() {
        var messageContent = "hello cloud stream";
        var message = MessageBuilder.withPayload(messageContent).build();
        this.inputDestination.send(message, this.inputTopic);

        assertEquals(messageContent, myService.messages.get(0));
    }
}

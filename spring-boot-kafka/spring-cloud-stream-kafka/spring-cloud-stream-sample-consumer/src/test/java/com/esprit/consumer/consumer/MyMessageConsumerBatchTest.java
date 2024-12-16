package com.esprit.consumer.consumer;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.ImportAutoConfiguration;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.cloud.stream.binder.test.InputDestination;
import org.springframework.cloud.stream.binder.test.TestChannelBinderConfiguration;
import org.springframework.integration.support.MessageBuilder;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;


@ImportAutoConfiguration(TestChannelBinderConfiguration.class)
@SpringBootTest(properties = {"spring.cloud.function.definition=myConsumerBatch"})
class MyMessageConsumerBatchTest {

    @Value("${spring.cloud.stream.bindings.myConsumerBatch-in-0.destination}")
    private String inputTopic;

    @Autowired
    private InputDestination inputDestination;

    @Autowired
    private MyService myService;

    @Test
    void shouldConsumeMessage() {
        var messageContent1 = "hello cloud stream 1";
        var messageContent2 = "hello cloud stream 2";

        var message = MessageBuilder.withPayload(List.of(messageContent1, messageContent2)).build();
        this.inputDestination.send(message, this.inputTopic);

        assertEquals(messageContent1, myService.messages.get(0));
    }
}
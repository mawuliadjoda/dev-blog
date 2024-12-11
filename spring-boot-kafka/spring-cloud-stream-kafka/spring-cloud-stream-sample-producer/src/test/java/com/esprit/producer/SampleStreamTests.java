package com.esprit.producer;

import com.esprit.producer.producer.MyMessageProducer;
import lombok.AllArgsConstructor;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.WebApplicationType;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.cloud.function.cloudevent.CloudEventMessageBuilder;
import org.springframework.cloud.stream.binder.test.EnableTestBinder;
import org.springframework.cloud.stream.binder.test.InputDestination;
import org.springframework.cloud.stream.binder.test.OutputDestination;
import org.springframework.cloud.stream.binder.test.TestChannelBinderConfiguration;
import org.springframework.cloud.stream.function.StreamBridge;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.messaging.Message;
import org.springframework.messaging.support.GenericMessage;

import java.util.function.Function;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

@SpringBootTest
public class SampleStreamTests {

    @Mock
    private MyMessageProducer myMessageProducer;

    @Test // myProducer-out-0
    void shouldSendMessage() {
        try (ConfigurableApplicationContext context = new SpringApplicationBuilder(
                TestChannelBinderConfiguration.getCompleteConfiguration(EmptyConfiguration.class))
                .web(WebApplicationType.NONE).run("--spring.jmx.enabled=false")) {

            StreamBridge streamBridge = context.getBean(StreamBridge.class);
            OutputDestination outputDestination = context.getBean(OutputDestination.class);
            Message<String> message = CloudEventMessageBuilder.withData("Test").build();
            streamBridge.send("fooDestination", message);
            myMessageProducer.sendMessage("Hello from Producer!");

            Message<byte[]> messageReceived = outputDestination.receive(1000, "fooDestination");
            Assertions.assertThat(new String(messageReceived.getPayload())).isEqualTo("Test");
        }
    }


    //@Autowired
    //private InputDestination inputDestination;
//
    //@Autowired
    //private OutputDestination outputDestination;

    @BeforeAll
    public static void before() {
        System.clearProperty("spring.cloud.function.definition");
    }

    //@Test
    //public void testEmptyConfiguration() {
    //    this.inputDestination.send(new GenericMessage<byte[]>("hello".getBytes()));
    //    assertThat(outputDestination.receive().getPayload()).isEqualTo("HELLO".getBytes());
    //}

    @Test
    public void sampleTest() {
        try (ConfigurableApplicationContext context = new SpringApplicationBuilder(
                TestChannelBinderConfiguration.getCompleteConfiguration(
                        SampleConfiguration.class))
                .run("--spring.cloud.function.definition=uppercase")) {
            InputDestination source = context.getBean(InputDestination.class);
            OutputDestination target = context.getBean(OutputDestination.class);
            source.send(new GenericMessage<byte[]>("hello".getBytes()));
            assertThat(target.receive().getPayload()).isEqualTo("HELLO".getBytes());
        }
    }



    @Test // myProducer-out-0
    void ensureEmptyPojoIsAllowed() {
        try (ConfigurableApplicationContext context = new SpringApplicationBuilder(
                TestChannelBinderConfiguration.getCompleteConfiguration(EmptyConfiguration.class))
                .web(WebApplicationType.NONE).run("--spring.jmx.enabled=false")) {

            StreamBridge streamBridge = context.getBean(StreamBridge.class);
            OutputDestination outputDestination = context.getBean(OutputDestination.class);
            Message<EmptyPojo> message = CloudEventMessageBuilder.withData(new EmptyPojo("test")).build();
            streamBridge.send("fooDestination", message);

            Message<byte[]> messageReceived = outputDestination.receive(1000, "fooDestination");
            Assertions.assertThat(new String(messageReceived.getPayload())).isEqualTo("{}");
        }
    }

    @EnableAutoConfiguration
    //@SpringBootApplication
    //@EnableTestBinder
    public static class SampleConfiguration {
        @Bean
        public Function<String, String> uppercase() {
            return v -> v.toUpperCase();
        }
    }

    @EnableAutoConfiguration
    public static class EmptyConfiguration {

    }
    @AllArgsConstructor
    public static class EmptyPojo {
        String name;
    }
}
package com.esprit.config;

import org.apache.kafka.clients.admin.NewTopic;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.config.TopicBuilder;

@Configuration
public class KafkaTopicConfig {

    @Bean
    public NewTopic personTopic() {
        return TopicBuilder.name("test-topic")
                .partitions(3)
                .replicas(3)
                .build();
    }
}

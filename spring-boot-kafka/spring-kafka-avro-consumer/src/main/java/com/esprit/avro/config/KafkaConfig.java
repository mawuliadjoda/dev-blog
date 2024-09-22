package com.esprit.avro.config;

import com.esprit.avro.model.Person;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.config.ConcurrentKafkaListenerContainerFactory;
import org.springframework.kafka.core.ConsumerFactory;

@Configuration
public class KafkaConfig {

    @Bean
    public ConcurrentKafkaListenerContainerFactory<String, Person> batchFactory(
            ConsumerFactory<String, Person> consumerFactory) {
        ConcurrentKafkaListenerContainerFactory<String, Person> factory =
                new ConcurrentKafkaListenerContainerFactory<>();
        factory.setConsumerFactory(consumerFactory);
        factory.setBatchListener(true);  // Active la consommation par lot
        return factory;
    }
}
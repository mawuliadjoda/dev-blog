
package com.esprit.product.config;

import org.springframework.context.annotation.Configuration;

@Configuration
public class KafkaProducerConfig {
    //@Bean
    //public ProducerFactory<String, Object> producerFactory(@Value("${spring.kafka.bootstrap-servers}") String brokers) {
    //  Map<String,Object> props = new HashMap<>();
    //  props.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, brokers);
    //  props.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, StringSerializer.class);
    //  props.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, JsonSerializer.class);
    //  return new DefaultKafkaProducerFactory<>(props);
    //}
    //@Bean public KafkaTemplate<String,Object> kafkaTemplate(ProducerFactory<String,Object> pf){ return new KafkaTemplate<>(pf); }
    //@Bean public NewTopic productUpdated(){ return TopicBuilder.name(com.example.productservice.adapters.outbound.messaging.Topics.PRODUCT_UPDATED).partitions(3).replicas(1).build(); }
}

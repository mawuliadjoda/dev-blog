
package com.esprit.order.config;



import java.util.HashMap;
import java.util.Map;
// @EnableKafka @Configuration
public class KafkaConfig {
  // @Bean ProducerFactory<String,Object> producerFactory(@Value("${spring.kafka.bootstrap-servers}") String brokers){
  //   Map<String,Object> p=new HashMap<>(); p.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, brokers);
  //   p.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, StringSerializer.class);
  //   p.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, JsonSerializer.class);
  //   return new DefaultKafkaProducerFactory<>(p);
  // }
  // @Bean KafkaTemplate<String,Object> kafkaTemplate(ProducerFactory<String,Object> pf){ return new KafkaTemplate<>(pf); }
  // @Bean ConsumerFactory<String,Object> consumerFactory(@Value("${spring.kafka.bootstrap-servers}") String brokers){
  //   Map<String,Object> p=new HashMap<>(); p.put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, brokers);
  //   p.put(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class);
  //   p.put(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, JsonDeserializer.class);
  //   p.put(JsonDeserializer.TRUSTED_PACKAGES, "*"); return new DefaultKafkaConsumerFactory<>(p);
  // }
  // @Bean ConcurrentKafkaListenerContainerFactory<String,Object> kafkaListenerContainerFactory(ConsumerFactory<String,Object> cf){
  //   var f=new ConcurrentKafkaListenerContainerFactory<String,Object>(); f.setConsumerFactory(cf); return f;
  // }
  // @Bean NewTopic orderPlaced(){return TopicBuilder.name(com.example.orderservice.adapters.outbound.messaging.Topics.ORDER_PLACED).partitions(3).replicas(1).build();}
  // @Bean NewTopic orderFailed(){return TopicBuilder.name(com.example.orderservice.adapters.outbound.messaging.Topics.ORDER_FAILED).partitions(3).replicas(1).build();}
  // @Bean NewTopic productUpdated(){return TopicBuilder.name(com.example.orderservice.adapters.outbound.messaging.Topics.PRODUCT_UPDATED).partitions(3).replicas(1).build();}
}

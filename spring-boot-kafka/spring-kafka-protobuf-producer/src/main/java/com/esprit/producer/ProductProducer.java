package com.esprit.producer;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

import dev.pravin.proto.product.Product;

@Service
public class ProductProducer {
    private static final String TOPIC = "product-topic";



    @Autowired
    private KafkaTemplate<String, Product> kafkaTemplate;

    public void sendProduct(Product product) {
        kafkaTemplate.send(TOPIC, product);
    }

}

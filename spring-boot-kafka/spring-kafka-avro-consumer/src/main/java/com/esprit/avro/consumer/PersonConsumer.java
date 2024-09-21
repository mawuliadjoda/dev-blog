package com.esprit.avro.consumer;

import com.esprit.avro.model.Person;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;
import org.springframework.messaging.Message;

@Service
public class PersonConsumer {

    @KafkaListener(topics = "person-topic", groupId = "person-consumer-group")
    public void consume(Message<Person> personMessage) {
        System.out.println("Received Person: " + personMessage);
    }
}
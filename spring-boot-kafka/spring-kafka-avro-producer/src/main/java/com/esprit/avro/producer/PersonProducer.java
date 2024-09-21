package com.esprit.avro.producer;


import com.esprit.avro.model.Person;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

@Service
public class PersonProducer {

    private static final String TOPIC = "person-topic";

    @Autowired
    private KafkaTemplate<String, Person> kafkaTemplate;

    public void sendPerson(Person person) {
        kafkaTemplate.send(TOPIC, person);
    }
}
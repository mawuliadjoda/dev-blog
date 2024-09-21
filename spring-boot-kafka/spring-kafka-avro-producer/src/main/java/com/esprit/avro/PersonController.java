package com.esprit.avro;

import com.esprit.avro.model.Person;
import com.esprit.avro.producer.PersonProducer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;


@RestController
public class PersonController {

    @Autowired
    private PersonProducer personProducer;

    @GetMapping("/send")
    public String sendPerson() {
        Person person = Person.newBuilder()
                .setFirstName("John")
                .setLastName("Doe")
                .setAge(30)
                .build();
        personProducer.sendPerson(person);
        return "Person sent!";
    }
}
package com.esprit.avro.consumer;

import com.esprit.avro.model.Person;
import lombok.extern.log4j.Log4j2;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.support.KafkaHeaders;
import org.springframework.stereotype.Service;
import org.springframework.messaging.Message;

import java.util.Map;

@Service
@Log4j2
public class PersonConsumer {

    /**
     * si besoin que du contenu principal du message, et aucun besoin des métadonnées, utiliser simplement
     * @param person
     */
    /*
    @KafkaListener(topics = "person-topic", groupId = "person-consumer-group")
    public void consume(Person person) {
        System.out.println("Received Person: " + person);
    }
     */


    /**
     * Si besoin d’accéder aux en-têtes de Spring Kafka et au payload, mais sans avoir besoin de gérer les détails techniques comme l'offset, Message<T> est suffisant
     * @param personMessage
     */
    @KafkaListener(topics = "person-topic", groupId = "person-consumer-group")
    public void consume(Message<Person> personMessage) {
        System.out.println("Received Person: " + personMessage);

        Map<String, Object> headers = personMessage.getHeaders();

        log.info("Consumed message from topic: {},Receive topic: {}, partition: {}, offset: {}, key: {}, groupId: {}, partition: {}, receivePartition: {}, payload: {}",
                headers.get(KafkaHeaders.TOPIC),
                headers.get(KafkaHeaders.RECEIVED_TOPIC),
                headers.get(KafkaHeaders.RECEIVED_TOPIC),
                headers.get(KafkaHeaders.OFFSET),
                headers.get(KafkaHeaders.RECEIVED_KEY),
                headers.get(KafkaHeaders.GROUP_ID),
                headers.get(KafkaHeaders.PARTITION),
                headers.get(KafkaHeaders.RECEIVED_PARTITION),
                personMessage.getPayload());
    }


    /**
     * besoin d’un accès très détaillé aux métadonnées Kafka, comme la partition, l’offset, la clé du message, et que tu veux contrôler finement comment tu consommes les messages, utilise ConsumerRecord
     * @param record
     */
    /*
    @KafkaListener(topics = "person-topic", groupId = "person-consumer-group")
    public void listen(ConsumerRecord<String, Person> record) {
        log.info("Consumed message from topic: {}, partition: {}, offset: {}, key: {}, timestamp: {}, payload: {}",
                record.topic(),
                record.partition(),
                record.offset(),
                record.key(),
                record.timestamp(),
                record.value());
    }
     */


}
package com.esprit.consumer;


import com.esprit.proto.product.Product;
import lombok.extern.log4j.Log4j2;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.messaging.Message;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@Log4j2
public class ProductConsumer {

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

     */
   /*
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
    */

    /**
     * consommation par lot
     * @param productMessages
     */
    @KafkaListener(topics = "product-topic", groupId = "product-protobuf-consumer", containerFactory = "batchFactory")
    public void consumeBatch(List<Message<Product>> productMessages) {
        System.out.println("Received batch of products: " + productMessages.size());
        for (Message<Product> productMessage : productMessages) {
            System.out.println("Product: " + productMessage.getPayload());
        }
        System.out.println("-- ----End processing " + productMessages.size());
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
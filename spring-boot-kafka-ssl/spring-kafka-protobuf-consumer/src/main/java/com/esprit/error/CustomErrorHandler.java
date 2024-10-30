package com.esprit.error;

import lombok.extern.log4j.Log4j2;
import org.apache.kafka.clients.consumer.Consumer;
import org.springframework.kafka.listener.ConsumerAwareListenerErrorHandler;
import org.springframework.kafka.listener.ListenerExecutionFailedException;
import org.springframework.messaging.Message;
import org.springframework.stereotype.Component;

@Component
@Log4j2
public class CustomErrorHandler implements ConsumerAwareListenerErrorHandler {


    @Override
    public Object handleError(Message<?> message, ListenerExecutionFailedException exception, Consumer<?, ?> consumer) {
        // Logique de gestion de l'erreur
        log.error("Erreur lors de la consommation du message: {}", message.getPayload());
        log.error("Exception: {}", exception.getMessage());

        // Accéder aux détails du consommateur Kafka (comme les partitions et offsets)
        consumer.assignment().forEach(partition -> {
            long position = consumer.position(partition);
            log.error("Partition: {}, Offset: {} " ,partition.partition() ,", Offset: " + position);
        });

        // Retourner un objet si nécessaire ou gérer le retour en fonction de votre logique.
        // Par exemple, vous pouvez retourner null ou une réponse spécifique.
        return null;
    }
}
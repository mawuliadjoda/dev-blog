
package com.esprit.product.adapters.outbound.messaging;

import com.esprit.product.adapters.outbound.messaging.events.ProductUpdatedEvent;
import com.esprit.product.domain.model.Product;
import com.esprit.product.domain.ports.ProductEventPublisher;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class KafkaProductEventPublisher implements ProductEventPublisher {
    //private final KafkaTemplate<String, Object> kafka;

    public void publishProductUpdated(Product p) {
        var evt = ProductUpdatedEvent.builder().productId(p.getId()).sku(p.getSku()).name(p.getName()).availableQuantity(p.getQuantity()).price(p.getPrice()).build();
        //kafka.send(Topics.PRODUCT_UPDATED, p.getId(), evt);
    }
}

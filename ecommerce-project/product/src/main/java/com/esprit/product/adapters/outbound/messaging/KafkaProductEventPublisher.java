
package com.esprit.product.adapters.outbound.messaging;

import com.esprit.product.adapters.outbound.messaging.events.ProductUpdatedEvent;
import com.esprit.product.domain.model.Product;
import com.esprit.product.domain.ports.ProductEventPublisher;
import lombok.RequiredArgsConstructor;
import org.springframework.cloud.stream.function.StreamBridge;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class KafkaProductEventPublisher implements ProductEventPublisher {
    private final StreamBridge streamBridge;

    public void publishProductUpdated(Product product) {
        var evt = ProductUpdatedEvent.builder().productId(product.getId()).sku(product.getSku()).name(product.getName()).availableQuantity(product.getQuantity()).price(product.getPrice()).build();
        streamBridge.send("produceProductMessage-out-0", evt);
    }
}

package com.esprit.application.ports.output;

import com.esprit.domain.event.ProductCreatedEvent;

public interface ProductEventPublisher {
    void publishProductCreatedEvent(ProductCreatedEvent event);
}

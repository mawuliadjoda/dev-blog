
package com.esprit.order.adapters.outbound.messaging.events;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ProductUpdatedEvent {
    private String productId;
    private String name;
    private int availableQuantity;
    private double price;
}

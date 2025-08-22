
package com.esprit.order.adapters.outbound.messaging.events;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class OrderPlacedEvent {
    private String orderId;
    private String customerEmail;
    private List<OrderedItem> items;
    private double totalAmount;

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    public static class OrderedItem {
        private String productId;
        private String sku;
        private int quantity;
        private double price;
    }
}

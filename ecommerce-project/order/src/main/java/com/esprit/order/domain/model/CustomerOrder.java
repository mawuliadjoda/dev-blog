
package com.esprit.order.domain.model;

import lombok.*;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CustomerOrder {
    private String id;
    private String customerEmail;
    private OrderStatus status;
    private double totalAmount;
    @Builder.Default
    private List<OrderItem> items = new ArrayList<>();
}

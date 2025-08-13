
package com.esprit.order.adapters.outbound.messaging;


import com.esprit.order.domain.model.CustomerOrder;
import com.esprit.order.domain.ports.OrderEventPublisher;
import lombok.RequiredArgsConstructor;

import org.springframework.stereotype.Component;

import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
public class KafkaOrderEventPublisher implements OrderEventPublisher {
    // private final KafkaTemplate<String, Object> kafka;

    public void orderPlaced(CustomerOrder order) {
        //var evt = OrderPlacedEvent.builder()
        //        .orderId(order.getId()).customerEmail(order.getCustomerEmail()).totalAmount(order.getTotalAmount())
        //        .items(order.getItems().stream().map(i -> OrderPlacedEvent.OrderedItem.builder().productId(i.getProductId()).sku(i.getSku()).quantity(i.getQuantity()).price(i.getPrice()).build()).collect(Collectors.toList()))
        //        .build();
        //kafka.send(Topics.ORDER_PLACED, order.getId(), evt);
    }

    public void orderFailed(CustomerOrder order, String reason) {
        //kafka.send(Topics.ORDER_FAILED, order.getId(), new OrderFailedEvent(order.getId(), reason));
    }
}

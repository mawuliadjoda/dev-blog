
package com.esprit.order.adapters.outbound.persistence;

import com.esprit.order.domain.model.CustomerOrder;
import com.esprit.order.domain.model.OrderItem;
import com.esprit.order.domain.model.OrderStatus;
import com.esprit.order.domain.ports.OrderRepositoryPort;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
public class OrderRepositoryAdapter implements OrderRepositoryPort {
    private final OrderRepository jpa;

    private static CustomerOrder toDomain(OrderEntity e) {
        var o = CustomerOrder.builder().id(e.getId()).customerEmail(e.getCustomerEmail()).status(OrderStatus.valueOf(e.getStatus())).totalAmount(e.getTotalAmount()).build();
        o.setItems(e.getItems().stream().map(OrderRepositoryAdapter::toDomain).collect(Collectors.toList()));
        return o;
    }

    private static OrderItem toDomain(OrderItemEntity e) {
        return OrderItem.builder().productId(e.getProductId()).quantity(e.getQuantity()).price(e.getPrice()).build();
    }

    private static OrderEntity toEntity(CustomerOrder o) {
        var e = OrderEntity.builder().id(o.getId()).customerEmail(o.getCustomerEmail()).status(o.getStatus().name()).totalAmount(o.getTotalAmount()).build();
        e.setItems(o.getItems().stream().map(i -> OrderItemEntity.builder().order(e).productId(i.getProductId()).quantity(i.getQuantity()).price(i.getPrice()).build()).collect(Collectors.toList()));
        return e;
    }

    public CustomerOrder save(CustomerOrder order) {
        return toDomain(jpa.save(toEntity(order)));
    }

    public Optional<CustomerOrder> findById(String id) {
        return jpa.findById(id).map(OrderRepositoryAdapter::toDomain);
    }

    public List<CustomerOrder> findAll() {
        return jpa.findAll().stream().map(OrderRepositoryAdapter::toDomain).collect(Collectors.toList());
    }
}

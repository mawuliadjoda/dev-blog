
package com.esprit.order.domain.service;

import com.esprit.order.domain.model.CustomerOrder;
import com.esprit.order.domain.model.OrderItem;
import com.esprit.order.domain.model.OrderStatus;
import com.esprit.order.domain.ports.OrderEventPublisher;
import com.esprit.order.domain.ports.OrderRepositoryPort;
import lombok.RequiredArgsConstructor;

import java.util.List;

@RequiredArgsConstructor
public class OrderUseCases {
    private final OrderRepositoryPort repo;
    private final OrderEventPublisher publisher;

    public CustomerOrder place(String email, List<OrderItem> items) {
        var order = CustomerOrder.builder().customerEmail(email).status(OrderStatus.PENDING).items(items).build();
        double total = items.stream().mapToDouble(i -> i.getPrice() * i.getQuantity()).sum();
        order.setTotalAmount(total);
        boolean invalid = items.stream().anyMatch(i -> i.getQuantity() <= 0);
        if (invalid) {
            order.setStatus(OrderStatus.FAILED);
            var saved = repo.save(order);
            publisher.orderFailed(saved, "Invalid quantity");
            return saved;
        }
        order.setStatus(OrderStatus.PLACED);
        var saved = repo.save(order);
        publisher.orderPlaced(saved);
        return saved;
    }

    public void onProductOutOfStock(String productId) {
        for (var o : repo.findAll()) {
            if (o.getStatus() == OrderStatus.PENDING && o.getItems().stream().anyMatch(i -> productId.equals(i.getProductId()))) {
                o.setStatus(OrderStatus.ON_HOLD);
                repo.save(o);
            }
        }
    }
}

package com.esprit.order.peristence.repository;

import com.esprit.order.peristence.entities.OrderItem;
import org.springframework.data.jpa.repository.JpaRepository;

public interface OrderItemRepository extends JpaRepository<OrderItem, Long> {
}

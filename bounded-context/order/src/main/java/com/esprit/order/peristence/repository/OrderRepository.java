package com.esprit.order.peristence.repository;

import com.esprit.order.peristence.entities.Order;
import org.springframework.data.jpa.repository.JpaRepository;

public interface OrderRepository extends JpaRepository<Order, Long> {
    // Additional query methods if needed
}

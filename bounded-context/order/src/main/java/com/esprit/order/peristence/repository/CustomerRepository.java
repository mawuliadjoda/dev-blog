package com.esprit.order.peristence.repository;

import com.esprit.order.peristence.entities.Customer;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CustomerRepository extends JpaRepository<Customer, Long> {
}

package com.esprit.inventory.persistence.repository;

import com.esprit.inventory.persistence.entities.Product;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ProductRepository extends JpaRepository<Product, Long> {
}

package com.esprit.common.persistence.repository;

import com.esprit.common.persistence.entities.ProductEntity;
import org.springframework.data.jpa.repository.JpaRepository;


public interface ProductCommonRepository extends JpaRepository<ProductEntity, Long> {
}
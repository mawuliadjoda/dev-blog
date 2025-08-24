
package com.esprit.product.adapters.outbound.persistence.repository;

import com.esprit.product.adapters.outbound.persistence.entity.ProductEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface ProductRepository extends JpaRepository<ProductEntity, String> {
    Optional<ProductEntity> findBySku(String sku);
}

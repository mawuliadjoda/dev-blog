
package com.esprit.product.adapters.outbound.persistence;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface SpringDataProductRepository extends JpaRepository<ProductEntity, String> {
    Optional<ProductEntity> findBySku(String sku);
}

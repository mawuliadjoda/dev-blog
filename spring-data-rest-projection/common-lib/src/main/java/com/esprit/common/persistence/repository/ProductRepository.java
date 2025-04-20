package com.esprit.common.persistence.repository;


import com.esprit.common.persistence.entities.ProductEntity;
import com.esprit.common.persistence.projection.ProductSummary;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

@RepositoryRestResource(
        collectionResourceRel = "products",
        path = "products",
        excerptProjection = ProductSummary.class
)
public interface ProductRepository extends JpaRepository<ProductEntity, Long> {
}
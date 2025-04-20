package com.esprit.common.persistence.repository;


import com.esprit.common.persistence.entities.ProductEntity;
import com.esprit.common.persistence.projection.ProductSummary;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.repository.query.Param;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;
import org.springframework.data.rest.core.annotation.RestResource;

import java.util.List;

@RepositoryRestResource(
        collectionResourceRel = "products",
        path = "products"
        // ,excerptProjection = ProductSummary.class
)
public interface ProductRepository extends JpaRepository<ProductEntity, Long>, JpaSpecificationExecutor<ProductEntity>, CustomRepository<ProductEntity> {

    @RestResource(path = "by-name")
    List<ProductEntity> search(@Param("name") String name);
}
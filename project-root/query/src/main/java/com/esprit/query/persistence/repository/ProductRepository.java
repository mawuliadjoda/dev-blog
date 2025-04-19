package com.esprit.query.persistence.repository;


import com.esprit.common.persistence.repository.ProductCommonRepository;
import com.esprit.query.persistence.projection.ProductSummary;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

@RepositoryRestResource(
        collectionResourceRel = "products",
        path = "products",
        excerptProjection = ProductSummary.class
)
public interface ProductRepository extends ProductCommonRepository {
}
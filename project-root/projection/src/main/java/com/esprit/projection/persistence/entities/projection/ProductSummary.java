package com.esprit.projection.persistence.entities.projection;


import com.esprit.projection.persistence.entities.ProductEntity;
import org.springframework.data.rest.core.config.Projection;

@Projection(name = "productSummary", types = ProductEntity.class)
public interface ProductSummary {
    Long getId();
    String getName();
    double getPrice();

    default String getProjectionType() {
        return "SUMMARY";
    }
}
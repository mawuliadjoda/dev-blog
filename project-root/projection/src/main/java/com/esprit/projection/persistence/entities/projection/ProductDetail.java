package com.esprit.projection.persistence.entities.projection;


import com.esprit.projection.persistence.entities.ProductEntity;
import org.springframework.data.rest.core.config.Projection;

// https://docs.spring.io/spring-data/rest/reference/projections-excerpts.html
@Projection(name = "productDetail", types = ProductEntity.class)
public interface ProductDetail {
    Long getId();
    String getName();
    double getPrice();
    String getDescription();

    default String getProjectionType() {
        return "DETAIL";
    }
}
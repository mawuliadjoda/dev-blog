package com.esprit.common.persistence.projection;


import com.esprit.common.persistence.entities.ProductEntity;
import org.springframework.data.rest.core.config.Projection;

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
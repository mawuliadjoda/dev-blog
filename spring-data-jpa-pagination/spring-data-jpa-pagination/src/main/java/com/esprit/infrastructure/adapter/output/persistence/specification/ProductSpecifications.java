package com.esprit.infrastructure.adapter.output.persistence.specification;

import com.esprit.infrastructure.adapter.output.persistence.entity.ProductEntity;
import org.springframework.data.jpa.domain.Specification;

public class ProductSpecifications {
    public static Specification<ProductEntity> nameContains(String name) {
        return (root, query, criteriaBuilder) ->
                name == null ? null : criteriaBuilder.like(root.get("name"), "%" + name + "%");
    }

    public static Specification<ProductEntity> descriptionContains(String description) {
        return (root, query, criteriaBuilder) ->
                description == null ? null : criteriaBuilder.like(root.get("description"), "%" + description + "%");
    }
}

package com.esprit.common.persistence.specification;

import com.esprit.common.persistence.entities.ProductEntity;
import org.springframework.data.jpa.domain.Specification;

public interface ProductSpecification {

    static Specification<ProductEntity> byName(String name) {
        return (root, query, cb) -> cb.equal(root.get("name"), name);
    }
}

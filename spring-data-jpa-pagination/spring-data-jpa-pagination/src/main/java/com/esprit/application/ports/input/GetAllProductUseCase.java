package com.esprit.application.ports.input;

import com.esprit.domain.model.Product;
import com.esprit.domain.search.ProductDynamicSearchCriteria;
import com.esprit.domain.search.ProductSearchCriteria;
import com.esprit.infrastructure.adapter.output.persistence.entity.ProductEntity;
import org.springframework.data.jpa.domain.Specification;

import java.util.List;

public interface GetAllProductUseCase {
    // List<Product> findAll(Specification<ProductEntity> specification);
    List<Product> findAll(ProductSearchCriteria productSearchCriteria);

    List<Product> findAll(ProductDynamicSearchCriteria productDynamicSearchCriteria);
}

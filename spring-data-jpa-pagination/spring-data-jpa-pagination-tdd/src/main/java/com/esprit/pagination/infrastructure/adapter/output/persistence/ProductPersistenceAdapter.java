package com.esprit.pagination.infrastructure.adapter.output.persistence;

import com.esprit.pagination.domain.model.Product;
import com.esprit.pagination.ports.output.ProductOutputPort;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class ProductPersistenceAdapter implements ProductOutputPort {

    @Override
    public Product createProduct(Product product) {
        return null;
    }

    @Override
    public List<Product> findAllProducts() {
        return List.of();
    }
}

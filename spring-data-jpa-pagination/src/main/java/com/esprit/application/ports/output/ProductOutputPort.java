package com.esprit.application.ports.output;

import com.esprit.domain.model.Product;

import java.util.Optional;

public interface ProductOutputPort {
    Product saveProduct(Product product);

    Optional<Product> getProductById(Long id);
}

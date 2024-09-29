package com.esprit.application.ports.input;

import com.esprit.domain.model.Product;

public interface CreateProductUseCase {
    Product createProduct(Product product);
}

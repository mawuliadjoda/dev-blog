package com.esprit.pagination.ports.input;

import com.esprit.pagination.domain.model.Product;

public interface CreateProductUseCase {

    Product createProduct(Product product);
}

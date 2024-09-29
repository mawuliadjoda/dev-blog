package com.esprit.pagination.ports.input;

import com.esprit.pagination.domain.model.Product;

import java.util.List;

public interface GetAllProductsUseCase {
    List<Product> findAllProducts();
}

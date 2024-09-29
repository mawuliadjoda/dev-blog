package com.esprit.pagination.ports.output;

import com.esprit.pagination.domain.model.Product;

import java.util.List;

public interface ProductOutputPort {
    Product createProduct(Product product);

    List<Product> findAllProducts();
}

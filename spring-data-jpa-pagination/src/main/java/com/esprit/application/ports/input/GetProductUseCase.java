package com.esprit.application.ports.input;

import com.esprit.domain.model.Product;

public interface GetProductUseCase {
    Product getProductById(Long id);
}

package com.esprit.domain.service;

import com.esprit.application.ports.input.CreateProductUseCase;
import com.esprit.application.ports.input.GetProductUseCase;
import com.esprit.application.ports.output.ProductEventPublisher;
import com.esprit.application.ports.output.ProductOutputPort;
import com.esprit.domain.event.ProductCreatedEvent;
import com.esprit.domain.exception.ProductNotFound;
import com.esprit.domain.model.Product;
import lombok.AllArgsConstructor;

@AllArgsConstructor
public class ProductService implements CreateProductUseCase, GetProductUseCase {

    private final ProductOutputPort productOutputPort;
    private final ProductEventPublisher productEventPublisher;

    @Override
    public Product createProduct(Product product) {
        product = productOutputPort.saveProduct(product);
        productEventPublisher.publishProductCreatedEvent(new ProductCreatedEvent(product.getId()));

        return product;
    }

    @Override
    public Product getProductById(Long id) {
        return productOutputPort.getProductById(id).orElseThrow(()-> new ProductNotFound("Product not found with id "+ id));
    }
}

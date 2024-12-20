package com.esprit.domain.service;

import com.esprit.application.ports.input.CreateProductUseCase;
import com.esprit.application.ports.input.GetAllProductUseCase;
import com.esprit.application.ports.input.GetProductUseCase;
import com.esprit.application.ports.output.ProductEventPublisher;
import com.esprit.application.ports.output.ProductOutputPort;
import com.esprit.domain.event.ProductCreatedEvent;
import com.esprit.domain.exception.ProductNotFound;
import com.esprit.domain.model.Product;
import com.esprit.domain.search.ProductSearchCriteria;
import com.esprit.infrastructure.adapter.output.persistence.entity.ProductEntity;
import lombok.AllArgsConstructor;
import org.springframework.data.jpa.domain.Specification;

import java.util.List;

@AllArgsConstructor
public class ProductService implements CreateProductUseCase, GetProductUseCase, GetAllProductUseCase {

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

    @Override
    public List<Product> findAll(ProductSearchCriteria productSearchCriteria) {
        return productOutputPort.findAll(productSearchCriteria);
    }


    // @Override
    // public List<Product> findAll(Specification<ProductEntity> specification) {
    //     return productOutputPort.findAll(specification);
    // }
}


package com.esprit.product.adapters.outbound.persistence;

import com.esprit.product.domain.model.Product;
import com.esprit.product.domain.ports.ProductRepositoryPort;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.Optional;
import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
public class ProductRepositoryAdapter implements ProductRepositoryPort {
    private final SpringDataProductRepository jpa;

    private static Product toDomain(ProductEntity e) {
        return Product.builder().id(e.getId()).sku(e.getSku()).name(e.getName()).price(e.getPrice()).quantity(e.getQuantity()).build();
    }

    private static ProductEntity toEntity(Product d) {
        return ProductEntity.builder().id(d.getId()).sku(d.getSku()).name(d.getName()).price(d.getPrice()).quantity(d.getQuantity()).build();
    }

    public Product save(Product p) {
        return toDomain(jpa.save(toEntity(p)));
    }

    public Optional<Product> findById(String id) {
        return jpa.findById(id).map(ProductRepositoryAdapter::toDomain);
    }

    public Optional<Product> findBySku(String sku) {
        return jpa.findBySku(sku).map(ProductRepositoryAdapter::toDomain);
    }

    public java.util.List<Product> findAll() {
        return jpa.findAll().stream().map(ProductRepositoryAdapter::toDomain).collect(Collectors.toList());
    }
}

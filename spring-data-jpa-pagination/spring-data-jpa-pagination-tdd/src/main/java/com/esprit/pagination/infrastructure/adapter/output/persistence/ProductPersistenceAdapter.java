package com.esprit.pagination.infrastructure.adapter.output.persistence;

import com.esprit.pagination.domain.model.Product;
import com.esprit.pagination.infrastructure.adapter.output.persistence.entity.ProductEntity;
import com.esprit.pagination.infrastructure.adapter.output.persistence.mapper.ProductPersistenceMapper;
import com.esprit.pagination.infrastructure.adapter.output.persistence.repository.ProductRepository;
import com.esprit.pagination.ports.output.ProductOutputPort;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@RequiredArgsConstructor
public class ProductPersistenceAdapter implements ProductOutputPort {

    private final ProductRepository productRepository;
    private final ProductPersistenceMapper productPersistenceMapper;

    @Override
    public Product createProduct(Product product) {
        ProductEntity productEntity = productPersistenceMapper.toProductEntity(product);
        ProductEntity savedProduct = productRepository.save(productEntity);
        return productPersistenceMapper.toProduct(savedProduct);
    }

    @Override
    public List<Product> findAllProducts() {
        List<ProductEntity> allProducts = productRepository.findAll();

        return productPersistenceMapper.toProducts(allProducts);
    }
}

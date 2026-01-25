
package com.studpay.template.adapters.outbound.persistence;


import com.studpay.template.adapters.outbound.persistence.mapper.ProductPersistenceMapper;
import com.studpay.template.adapters.outbound.persistence.repository.ProductRepository;
import com.studpay.template.domain.model.Product;
import com.studpay.template.domain.ports.ProductRepositoryPort;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;

@Component
@RequiredArgsConstructor
public class ProductRepositoryAdapter implements ProductRepositoryPort {
    private final ProductRepository productRepository;
    private final ProductPersistenceMapper productPersistenceMapper;

    public Product save(Product p) {
        return productPersistenceMapper.toDomain(productRepository.save(productPersistenceMapper.toEntity(p)));
    }

    public Optional<Product> findById(String id) {
        return productRepository.findById(id).map(productPersistenceMapper::toDomain);
    }

    public Optional<Product> findByName(String name) {
        return productRepository.findByName(name).map(productPersistenceMapper::toDomain);
    }

    public List<Product> findAll() {
        return productPersistenceMapper.toDomains(productRepository.findAll());
    }

    @Override
    public void deleteById(String id) {
        productRepository.deleteById(id);
    }
}

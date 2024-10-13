package com.esprit.pagination.infrastructure.adapter.output.persistence;

import com.esprit.pagination.domain.model.PaginatedData;
import com.esprit.pagination.domain.model.Product;
import com.esprit.pagination.domain.model.common.PageableQueryRequest;
import com.esprit.pagination.infrastructure.adapter.output.persistence.entity.ProductEntity;
import com.esprit.pagination.infrastructure.adapter.output.persistence.mapper.PageablePersistenceMapper;
import com.esprit.pagination.infrastructure.adapter.output.persistence.mapper.ProductPersistenceMapper;
import com.esprit.pagination.infrastructure.adapter.output.persistence.repository.ProductRepository;
import com.esprit.pagination.ports.output.ProductOutputPort;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@RequiredArgsConstructor
public class ProductPersistenceAdapter implements ProductOutputPort {

    private final ProductRepository productRepository;
    private final ProductPersistenceMapper productPersistenceMapper;
    private final PageablePersistenceMapper pageablePersistenceMapper;


    @Override
    public Product createProduct(Product product) {
        ProductEntity productEntity = productPersistenceMapper.toEntity(product);
        ProductEntity savedProduct = productRepository.save(productEntity);
        return productPersistenceMapper.toDomain(savedProduct);
    }

    @Override
    public List<Product> findAllProducts() {
        List<ProductEntity> allProducts = productRepository.findAll();

        return productPersistenceMapper.toDomains(allProducts);
    }

    @Override
    public PaginatedData<Product> getAllPaginatedProducts(PageableQueryRequest pageableQueryRequest) {

        PageRequest pageable = pageablePersistenceMapper.toPageable(pageableQueryRequest);
        Page<ProductEntity> productEntityPage = productRepository.findAll(pageable);
        return productPersistenceMapper.toDomain(productEntityPage);
    }
}

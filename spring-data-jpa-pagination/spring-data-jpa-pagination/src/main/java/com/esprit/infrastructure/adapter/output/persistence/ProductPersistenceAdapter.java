package com.esprit.infrastructure.adapter.output.persistence;

import com.esprit.application.ports.output.ProductOutputPort;
import com.esprit.domain.model.Product;
import com.esprit.domain.search.ProductSearchCriteria;
import com.esprit.infrastructure.adapter.output.persistence.specification.ProductSpecifications;
import com.esprit.infrastructure.adapter.output.persistence.entity.ProductEntity;
import com.esprit.infrastructure.adapter.output.persistence.mapper.ProductPersistenceMapper;
import com.esprit.infrastructure.adapter.output.persistence.repository.ProductRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;

@Component
@RequiredArgsConstructor
public class ProductPersistenceAdapter implements ProductOutputPort {

    private final ProductRepository productRepository;
    private final ProductPersistenceMapper productPersistenceMapper;

    @Override
    public Product saveProduct(Product product) {
        ProductEntity productEntity = productPersistenceMapper.toProductEntity(product);
        productEntity = productRepository.save(productEntity);
        return productPersistenceMapper.toProduct(productEntity);
    }

    @Override
    public Optional<Product> getProductById(Long id) {
        Optional<ProductEntity> productEntity = productRepository.findById(id);

        if (productEntity.isEmpty()) {
            return Optional.empty();
        }

        Product product = productPersistenceMapper.toProduct(productEntity.get());
        return Optional.of(product);
    }

    @Override
    public List<Product> findAll(ProductSearchCriteria productSearchCriteria) {
        Specification<ProductEntity> spec = Specification.where(ProductSpecifications.nameContains(productSearchCriteria.getName()))
                .and(ProductSpecifications.descriptionContains(productSearchCriteria.getDescription()))
                .and(getPriceSpecification(productSearchCriteria.getPrice(), productSearchCriteria.getPriceOperation()));


        return productPersistenceMapper.toProducts(productRepository.findAll(spec));
    }


    private Specification<ProductEntity> getPriceSpecification(Double price, String priceOperation) {
        switch (priceOperation) {
            case "gt":
                return ProductSpecifications.hasPriceGreaterThan(price);
            case "lt":
                return ProductSpecifications.hasPriceLessThan(price);
            case "eq":
                return ProductSpecifications.hasPrice(price);
            default:
                return Specification.where(null);
        }
    }
    // @Override
    // public List<Product> findAll(Specification<ProductEntity> specification) {
    //     return productPersistenceMapper.toProducts(productRepository.findAll(specification));
    // }
}

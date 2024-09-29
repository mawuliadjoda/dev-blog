package com.esprit.pagination.infrastructure.adapter.config;

import com.esprit.pagination.domain.service.ProductService;
import com.esprit.pagination.infrastructure.adapter.output.persistence.ProductPersistenceAdapter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class BeanConfiguration {
    @Bean
    public ProductService productService(ProductPersistenceAdapter productPersistenceAdapter) {
        return new ProductService(productPersistenceAdapter);
    }
}

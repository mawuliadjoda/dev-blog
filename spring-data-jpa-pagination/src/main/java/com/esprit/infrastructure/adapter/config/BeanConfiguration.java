package com.esprit.infrastructure.adapter.config;

import com.esprit.domain.service.ProductService;
import com.esprit.infrastructure.adapter.output.eventpublisher.ProductEventPublisherAdapter;
import com.esprit.infrastructure.adapter.output.persistence.ProductPersistenceAdapter;
import com.esprit.infrastructure.adapter.output.persistence.mapper.ProductPersistenceMapper;
import com.esprit.infrastructure.adapter.output.persistence.repository.ProductRepository;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class BeanConfiguration {

   // @Bean
   // public ProductPersistenceAdapter productPersistenceAdapter(ProductRepository productRepository, ProductPersistenceMapper productPersistenceMapper) {
   //     return new ProductPersistenceAdapter(productRepository, productPersistenceMapper);
   // }

    @Bean
    public ProductEventPublisherAdapter productEventPublisherAdapter(ApplicationEventPublisher applicationEventPublisher) {
        return new ProductEventPublisherAdapter(applicationEventPublisher);
    }

    @Bean
    public ProductService productService(ProductPersistenceAdapter productPersistenceAdapter, ProductEventPublisherAdapter productEventPublisherAdapter) {
        return new ProductService(productPersistenceAdapter, productEventPublisherAdapter);
    }

}

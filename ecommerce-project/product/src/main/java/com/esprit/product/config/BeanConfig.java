
package com.esprit.product.config;

import com.esprit.product.domain.ports.ProductEventPublisher;
import com.esprit.product.domain.ports.ProductRepositoryPort;
import com.esprit.product.domain.service.ProductUseCases;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class BeanConfig {
    @Bean
    public ProductUseCases productUseCases(ProductRepositoryPort repo, ProductEventPublisher publisher) {
        return new ProductUseCases(repo, publisher);
    }
}

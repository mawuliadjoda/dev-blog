
package com.studpay.template.config;


import com.studpay.template.domain.ports.ProductEventPublisher;
import com.studpay.template.domain.ports.ProductRepositoryPort;
import com.studpay.template.domain.service.ProductUseCases;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class BeanConfig {
    @Bean
    public ProductUseCases productUseCases(ProductRepositoryPort repo, ProductEventPublisher publisher) {
        return new ProductUseCases(repo, publisher);
    }
}

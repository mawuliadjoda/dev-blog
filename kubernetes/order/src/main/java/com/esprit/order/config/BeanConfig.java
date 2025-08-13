
package com.esprit.order.config;


import com.esprit.order.domain.ports.OrderEventPublisher;
import com.esprit.order.domain.ports.OrderRepositoryPort;
import com.esprit.order.domain.service.OrderUseCases;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class BeanConfig {
    @Bean
    public OrderUseCases orderUseCases(OrderRepositoryPort repo, OrderEventPublisher publisher) {
        return new OrderUseCases(repo, publisher);
    }
}

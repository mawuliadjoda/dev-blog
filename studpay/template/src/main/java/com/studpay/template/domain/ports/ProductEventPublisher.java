
package com.esprit.product.domain.ports;


import com.esprit.product.domain.model.Product;

public interface ProductEventPublisher {
    void publishProductUpdated(Product product);
}

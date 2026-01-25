
package com.studpay.template.domain.ports;


import com.studpay.template.domain.model.Product;

public interface ProductEventPublisher {
    void publishProductUpdated(Product product);
}

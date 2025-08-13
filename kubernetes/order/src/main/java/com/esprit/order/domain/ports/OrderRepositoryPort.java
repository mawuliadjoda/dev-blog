
package com.esprit.order.domain.ports;


import com.esprit.order.domain.model.CustomerOrder;

import java.util.List;
import java.util.Optional;

public interface OrderRepositoryPort {
    CustomerOrder save(CustomerOrder order);

    Optional<CustomerOrder> findById(String id);

    List<CustomerOrder> findAll();
}

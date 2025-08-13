
package com.esprit.order.domain.ports;


import com.esprit.order.domain.model.CustomerOrder;

public interface OrderEventPublisher {
    void orderPlaced(CustomerOrder order);

    void orderFailed(CustomerOrder order, String reason);
}

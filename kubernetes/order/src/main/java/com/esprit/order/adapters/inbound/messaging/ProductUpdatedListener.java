
package com.esprit.order.adapters.inbound.messaging;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
@Component @RequiredArgsConstructor
public class ProductUpdatedListener {
  // private final OrderUseCases useCases;
  // @KafkaListener(topics = Topics.PRODUCT_UPDATED, groupId = "order-service")
  // public void onProductUpdated(ConsumerRecord<String, ProductUpdatedEvent> record){
  //   if (record.value().getAvailableQuantity()==0){ useCases.onProductOutOfStock(record.value().getProductId()); }
  // }
}

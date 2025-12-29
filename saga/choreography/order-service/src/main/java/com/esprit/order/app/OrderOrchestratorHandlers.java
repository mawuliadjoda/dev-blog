package com.esprit.order.app;


import com.esprit.order.api.messages.*;
import com.esprit.order.domain.OrderRepository;
import com.esprit.order.domain.OrderStatus;
import com.esprit.order.infra.idempotence.ProcessedEvent;
import com.esprit.order.infra.idempotence.ProcessedEventRepository;
import com.esprit.order.infra.messaging.StreamPublisher;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;
import java.util.function.Consumer;

/**
 * order-service orchestrates the saga:
 * 1) Create order (PAYMENT_PENDING) -> send ReservePaymentCommand
 * 2) On PaymentReserved -> set STOCK_PENDING -> send ReserveStockCommand (contains sku/qty)
 * 3) On StockReserved -> COMPLETED -> emit OrderCompletedEvent
 * 4) On PaymentFailed or StockFailed -> CANCELLED -> emit OrderCancelledEvent (compensation triggers)
 */
@RequiredArgsConstructor
@Configuration
public class OrderOrchestratorHandlers {

  private final OrderRepository orders;
  private final ProcessedEventRepository processed;
  private final StreamPublisher publisher;



  @Bean
  public Consumer<PaymentReservedEvent> paymentReserved() { return this::onPaymentReserved; }

  @Bean
  public Consumer<PaymentFailedEvent> paymentFailed() { return this::onPaymentFailed; }

  @Bean
  public Consumer<StockReservedEvent> stockReserved() { return this::onStockReserved; }

  @Bean
  public Consumer<StockFailedEvent> stockFailed() { return this::onStockFailed; }

  @Transactional
  void onPaymentReserved(PaymentReservedEvent evt) {
    if (processed.existsById(evt.eventId())) return;
    processed.save(ProcessedEvent.builder().eventId(evt.eventId()).build());

    var orderId = UUID.fromString(evt.orderId());
    var o = orders.findById(orderId).orElseThrow();

    if (o.getStatus() == OrderStatus.CANCELLED || o.getStatus() == OrderStatus.COMPLETED) return;

    o.setStatus(OrderStatus.STOCK_PENDING);
    orders.save(o);

    // Orchestrated step #2: ask stock-service to reserve stock (no order_snapshot needed)
    publisher.send("reserveStockCommand-out-0",
        new ReserveStockCommand(UUID.randomUUID().toString(), evt.orderId(), o.getSku(), o.getQty())
    );
  }

  @Transactional
  void onPaymentFailed(PaymentFailedEvent evt) {
    if (processed.existsById(evt.eventId())) return;
    processed.save(ProcessedEvent.builder().eventId(evt.eventId()).build());

    cancel(evt.orderId(), evt.reason());
  }

  @Transactional
  void onStockReserved(StockReservedEvent evt) {
    if (processed.existsById(evt.eventId())) return;
    processed.save(ProcessedEvent.builder().eventId(evt.eventId()).build());

    var orderId = UUID.fromString(evt.orderId());
    var o = orders.findById(orderId).orElseThrow();

    if (o.getStatus() == OrderStatus.CANCELLED) return;

    o.setStatus(OrderStatus.COMPLETED);
    orders.save(o);

    publisher.send("orderCompleted-out-0",
        new OrderCompletedEvent(UUID.randomUUID().toString(), evt.orderId())
    );
  }

  @Transactional
  void onStockFailed(StockFailedEvent evt) {
    if (processed.existsById(evt.eventId())) return;
    processed.save(ProcessedEvent.builder().eventId(evt.eventId()).build());

    cancel(evt.orderId(), evt.reason());
  }

  private void cancel(String orderId, String reason) {
    var id = UUID.fromString(orderId);
    var o = orders.findById(id).orElseThrow();

    if (o.getStatus() == OrderStatus.CANCELLED || o.getStatus() == OrderStatus.COMPLETED) return;

    o.setStatus(OrderStatus.CANCELLED);
    o.setReason(reason);
    orders.save(o);

    // Compensation trigger: payment-service and stock-service listen to order.cancelled
    publisher.send("orderCancelled-out-0",
        new OrderCancelledEvent(UUID.randomUUID().toString(), orderId, reason)
    );
  }
}

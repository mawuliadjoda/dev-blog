package com.esprit.order.app;

import com.esprit.order.api.messages.*;
import com.esprit.order.domain.*;
import com.esprit.order.infra.idempotence.ProcessedEvent;
import com.esprit.order.infra.idempotence.ProcessedEventRepository;
import com.esprit.order.infra.messaging.StreamPublisher;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;
import java.util.function.Consumer;

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

    var o = orders.findById(UUID.fromString(evt.orderId())).orElseThrow();
    if (o.getStatus() == OrderStatus.CANCELLED || o.getStatus() == OrderStatus.COMPLETED) return;

    o.setStatus(OrderStatus.STOCK_PENDING);
    o.setCompletedStep(SagaStep.PAYMENT_RESERVED);
    orders.save(o);

    publisher.send("reserveStockCommand-out-0",
        new ReserveStockCommand(UUID.randomUUID().toString(), evt.orderId(), o.getSku(), o.getQty())
    );
  }

  @Transactional
  void onPaymentFailed(PaymentFailedEvent evt) {
    if (processed.existsById(evt.eventId())) return;
    processed.save(ProcessedEvent.builder().eventId(evt.eventId()).build());
    cancelAndCompensate(evt.orderId(), evt.reason());
  }

  @Transactional
  void onStockReserved(StockReservedEvent evt) {
    if (processed.existsById(evt.eventId())) return;
    processed.save(ProcessedEvent.builder().eventId(evt.eventId()).build());

    var o = orders.findById(UUID.fromString(evt.orderId())).orElseThrow();
    if (o.getStatus() == OrderStatus.CANCELLED) return;

    o.setStatus(OrderStatus.COMPLETED);
    o.setCompletedStep(SagaStep.STOCK_RESERVED);
    orders.save(o);

    publisher.send("orderCompleted-out-0",
        new OrderCompletedEvent(UUID.randomUUID().toString(), evt.orderId())
    );
  }

  @Transactional
  void onStockFailed(StockFailedEvent evt) {
    if (processed.existsById(evt.eventId())) return;
    processed.save(ProcessedEvent.builder().eventId(evt.eventId()).build());
    cancelAndCompensate(evt.orderId(), evt.reason());
  }

  private void cancelAndCompensate(String orderId, String reason) {
    var o = orders.findById(UUID.fromString(orderId)).orElseThrow();
    if (o.getStatus() == OrderStatus.CANCELLED || o.getStatus() == OrderStatus.COMPLETED) return;

    o.setStatus(OrderStatus.CANCELLED);
    o.setCancelReason(reason);
    orders.save(o);

    // explicit compensations
    if (o.getCompletedStep() == SagaStep.STOCK_RESERVED) {
      publisher.send("releaseStockCommand-out-0",
          new ReleaseStockCommand(UUID.randomUUID().toString(), orderId, reason)
      );
    }
    if (o.getCompletedStep() == SagaStep.PAYMENT_RESERVED || o.getCompletedStep() == SagaStep.STOCK_RESERVED) {
      publisher.send("cancelPaymentCommand-out-0",
          new CancelPaymentCommand(UUID.randomUUID().toString(), orderId, reason)
      );
    }

    publisher.send("orderCancelled-out-0",
        new OrderCancelledEvent(UUID.randomUUID().toString(), orderId, reason)
    );
  }
}

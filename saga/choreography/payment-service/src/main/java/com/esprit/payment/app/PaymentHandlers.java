package com.esprit.payment.app;


import com.esprit.payment.api.messages.OrderCancelledEvent;
import com.esprit.payment.api.messages.PaymentFailedEvent;
import com.esprit.payment.api.messages.PaymentReservedEvent;
import com.esprit.payment.api.messages.ReservePaymentCommand;
import com.esprit.payment.domain.PaymentReservationEntity;
import com.esprit.payment.domain.PaymentReservationRepository;
import com.esprit.payment.domain.PaymentStatus;
import com.esprit.payment.infra.idempotence.ProcessedEvent;
import com.esprit.payment.infra.idempotence.ProcessedEventRepository;
import com.esprit.payment.infra.messaging.StreamPublisher;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.UUID;
import java.util.function.Consumer;

/**
 * payment-service:
 * - Consumes command: payment.reserve.command (ReservePaymentCommand)
 * - Emits: payment.reserved OR payment.failed
 * - Compensation: listens order.cancelled -> marks payment reservation CANCELLED
 *
 * Simulation rule:
 * - amount > 1000 => PAYMENT_DECLINED
 */

@RequiredArgsConstructor
@Configuration
public class PaymentHandlers {

  private final PaymentReservationRepository reservations;
  private final ProcessedEventRepository processed;
  private final StreamPublisher publisher;


  @Bean
  public Consumer<ReservePaymentCommand> reservePaymentCommand() {
    return this::onReservePaymentCommand;
  }

  @Bean
  public Consumer<OrderCancelledEvent> orderCancelled() {
    return this::onOrderCancelled;
  }


  @Transactional
  void onReservePaymentCommand(ReservePaymentCommand cmd) {
    if (processed.existsById(cmd.eventId())) return;
    processed.save(ProcessedEvent.builder().eventId(cmd.eventId()).build());

    var orderId = UUID.fromString(cmd.orderId());

    // business idempotence: command replay => no double reservation
    if (reservations.existsById(orderId)) return;

    if (cmd.amount().compareTo(new BigDecimal("1000")) > 0) {
      publisher.send("paymentFailed-out-0", new PaymentFailedEvent(UUID.randomUUID().toString(), cmd.orderId(), "PAYMENT_DECLINED"));
      return;
    }

    var r = new PaymentReservationEntity();
    r.setOrderId(orderId);
    r.setStatus(PaymentStatus.RESERVED);
    r.setAmount(cmd.amount());
    reservations.save(r);

    publisher.send("paymentReserved-out-0",
        new PaymentReservedEvent(UUID.randomUUID().toString(), cmd.orderId())
    );
  }

  @Transactional
  void onOrderCancelled(OrderCancelledEvent evt) {
    if (processed.existsById(evt.eventId())) return;
    processed.save(ProcessedEvent.builder().eventId(evt.eventId()).build());

    var orderId = UUID.fromString(evt.orderId());
    var r = reservations.findById(orderId).orElse(null);
    if (r == null) return;

    if (r.getStatus() == PaymentStatus.CANCELLED) return;

    if (r.getStatus() == PaymentStatus.RESERVED) {
      r.setStatus(PaymentStatus.CANCELLED);
      reservations.save(r);
    }
  }
}

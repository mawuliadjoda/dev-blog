package com.esprit.payment.app;

import com.esprit.payment.api.messages.*;
import com.esprit.payment.domain.*;
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

@RequiredArgsConstructor
@Configuration
public class PaymentHandlers {

  private final PaymentReservationRepository reservations;
  private final ProcessedEventRepository processed;
  private final StreamPublisher publisher;

  @Bean
  public Consumer<ReservePaymentCommand> reservePaymentCommand() { return this::onReservePayment; }

  @Bean
  public Consumer<CancelPaymentCommand> cancelPaymentCommand() { return this::onCancelPayment; }

  @Transactional
  void onReservePayment(ReservePaymentCommand cmd) {
    if (processed.existsById(cmd.eventId())) return;
    processed.save(ProcessedEvent.builder().eventId(cmd.eventId()).build());

    var orderId = UUID.fromString(cmd.orderId());
    if (reservations.existsById(orderId)) return;

    if (cmd.amount().compareTo(new BigDecimal("1000")) > 0) {
      publisher.send("paymentFailed-out-0",
          new PaymentFailedEvent(UUID.randomUUID().toString(), cmd.orderId(), "PAYMENT_DECLINED")
      );
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
  void onCancelPayment(CancelPaymentCommand cmd) {
    if (processed.existsById(cmd.eventId())) return;
    processed.save(ProcessedEvent.builder().eventId(cmd.eventId()).build());

    var orderId = UUID.fromString(cmd.orderId());
    var r = reservations.findById(orderId).orElse(null);
    if (r == null) return;
    if (r.getStatus() == PaymentStatus.CANCELLED) return;

    r.setStatus(PaymentStatus.CANCELLED);
    reservations.save(r);
  }
}

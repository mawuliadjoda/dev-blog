package com.esprit.payment.domain;

import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;
import java.util.UUID;


@Getter
@Setter
@AllArgsConstructor
@RequiredArgsConstructor
@Builder

@Entity
@Table(name = "payment_reservations")
public class PaymentReservationEntity {

  @Id
  private UUID orderId;

  @Enumerated(EnumType.STRING)
  private PaymentStatus status;

  private BigDecimal amount;
}

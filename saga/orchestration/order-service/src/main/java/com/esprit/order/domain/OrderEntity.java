package com.esprit.order.domain;

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
@Table(name = "orders")
public class OrderEntity {
  @Id
  private UUID id;

  @Enumerated(EnumType.STRING)
  private OrderStatus status;

  @Enumerated(EnumType.STRING)
  private SagaStep completedStep;

  private BigDecimal amount;
  private String sku;
  private int qty;

  private String cancelReason;
}

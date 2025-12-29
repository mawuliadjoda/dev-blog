package com.esprit.stock.domain;

import jakarta.persistence.*;
import lombok.*;

import java.util.UUID;

@Getter
@Setter
@AllArgsConstructor
@RequiredArgsConstructor
@Builder

@Entity
@Table(name = "stock_reservations")
public class StockReservationEntity {
  @Id
  private UUID orderId;

  @Enumerated(EnumType.STRING)
  private StockStatus status;

  private String sku;
  private int qty;
}

package com.esprit.stock.domain;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@RequiredArgsConstructor
@Builder

@Entity
@Table(name = "inventory")
public class InventoryEntity {

  @Id
  private String sku;

  private int availableQty;
}

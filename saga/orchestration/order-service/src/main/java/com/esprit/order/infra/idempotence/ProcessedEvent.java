package com.esprit.order.infra.idempotence;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.*;

import java.time.Instant;

@Getter
@Setter
@AllArgsConstructor
@RequiredArgsConstructor
@Builder

@Entity
@Table(name = "processed_events")
public class ProcessedEvent {
  @Id
  private String eventId;

  @Builder.Default
  private Instant processedAt = Instant.now();
}

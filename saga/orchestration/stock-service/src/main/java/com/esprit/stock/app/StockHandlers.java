package com.esprit.stock.app;


import com.esprit.stock.api.messages.ReleaseStockCommand;
import com.esprit.stock.api.messages.ReserveStockCommand;
import com.esprit.stock.api.messages.StockFailedEvent;
import com.esprit.stock.api.messages.StockReservedEvent;
import com.esprit.stock.domain.InventoryRepository;
import com.esprit.stock.domain.StockReservationEntity;
import com.esprit.stock.domain.StockReservationRepository;
import com.esprit.stock.domain.StockStatus;
import com.esprit.stock.infra.idempotence.ProcessedEvent;
import com.esprit.stock.infra.idempotence.ProcessedEventRepository;
import com.esprit.stock.infra.messaging.StreamPublisher;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;
import java.util.function.Consumer;

@RequiredArgsConstructor
@Configuration
public class StockHandlers {

  private final InventoryRepository inventory;
  private final StockReservationRepository reservations;
  private final ProcessedEventRepository processed;
  private final StreamPublisher publisher;


  @Bean
  public Consumer<ReserveStockCommand> reserveStockCommand() { return this::onReserve; }

  @Bean
  public Consumer<ReleaseStockCommand> releaseStockCommand() { return this::onRelease; }

  @Transactional
  void onReserve(ReserveStockCommand cmd) {
    if (processed.existsById(cmd.eventId())) return;
    processed.save(ProcessedEvent.builder().eventId(cmd.eventId()).build());

    var orderId = UUID.fromString(cmd.orderId());
    if (reservations.existsById(orderId)) return;

    var inv = inventory.findById(cmd.sku()).orElse(null);
    if (inv == null) {
      publisher.send("stockFailed-out-0",
          new StockFailedEvent(UUID.randomUUID().toString(), cmd.orderId(), "UNKNOWN_SKU")
      );
      return;
    }
    if (inv.getAvailableQty() < cmd.qty()) {
      publisher.send("stockFailed-out-0",
          new StockFailedEvent(UUID.randomUUID().toString(), cmd.orderId(), "OUT_OF_STOCK")
      );
      return;
    }

    inv.setAvailableQty(inv.getAvailableQty() - cmd.qty());
    inventory.save(inv);

    var r = new StockReservationEntity();
    r.setOrderId(orderId);
    r.setSku(cmd.sku());
    r.setQty(cmd.qty());
    r.setStatus(StockStatus.RESERVED);
    reservations.save(r);

    publisher.send("stockReserved-out-0",
        new StockReservedEvent(UUID.randomUUID().toString(), cmd.orderId())
    );
  }

  @Transactional
  void onRelease(ReleaseStockCommand cmd) {
    if (processed.existsById(cmd.eventId())) return;
    processed.save(ProcessedEvent.builder().eventId(cmd.eventId()).build());

    var orderId = UUID.fromString(cmd.orderId());
    var r = reservations.findById(orderId).orElse(null);
    if (r == null) return;
    if (r.getStatus() == StockStatus.RELEASED) return;

    var inv = inventory.findById(r.getSku()).orElse(null);
    if (inv != null) {
      inv.setAvailableQty(inv.getAvailableQty() + r.getQty());
      inventory.save(inv);
    }
    r.setStatus(StockStatus.RELEASED);
    reservations.save(r);
  }
}

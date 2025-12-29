package com.esprit.stock.app;


import com.esprit.stock.api.messages.OrderCancelledEvent;
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

/**
 * stock-service (v2):
 * - Owns inventory + reservations
 * - Consumes command: stock.reserve.command (ReserveStockCommand)
 * - Emits: stock.reserved OR stock.failed
 * - Compensation: listens order.cancelled -> releases reservation and restores inventory
 */
@RequiredArgsConstructor
@Configuration
public class StockHandlers {

  private final InventoryRepository inventory;
  private final StockReservationRepository reservations;
  private final ProcessedEventRepository processed;
  private final StreamPublisher publisher;


  @Bean
  public Consumer<ReserveStockCommand> reserveStockCommand() {
    return this::onReserveStockCommand;
  }

  @Bean
  public Consumer<OrderCancelledEvent> orderCancelled() {
    return this::onOrderCancelled;
  }

  @Transactional
  void onReserveStockCommand(ReserveStockCommand cmd) {
    if (processed.existsById(cmd.eventId())) return;
    processed.save(ProcessedEvent.builder().eventId(cmd.eventId()).build());

    var orderId = UUID.fromString(cmd.orderId());

    // business idempotence: command replay => no double reservation
    if (reservations.existsById(orderId)) return;

    var inv = inventory.findById(cmd.sku()).orElse(null);
    if (inv == null) {
      publisher.send("stockFailed-out-0", new StockFailedEvent(UUID.randomUUID().toString(), cmd.orderId(), "UNKNOWN_SKU"));
      return;
    }

    if (inv.getAvailableQty() < cmd.qty()) {
      publisher.send("stockFailed-out-0", new StockFailedEvent(UUID.randomUUID().toString(), cmd.orderId(), "OUT_OF_STOCK"));
      return;
    }

    // reserve: decrement inventory + persist reservation
    inv.setAvailableQty(inv.getAvailableQty() - cmd.qty());
    inventory.save(inv);

    var r = new StockReservationEntity();
    r.setOrderId(orderId);
    r.setStatus(StockStatus.RESERVED);
    r.setSku(cmd.sku());
    r.setQty(cmd.qty());
    reservations.save(r);

    publisher.send("stockReserved-out-0", new StockReservedEvent(UUID.randomUUID().toString(), cmd.orderId()));
  }

  @Transactional
  void onOrderCancelled(OrderCancelledEvent evt) {
    if (processed.existsById(evt.eventId())) return;
    processed.save(ProcessedEvent.builder().eventId(evt.eventId()).build());

    var orderId = UUID.fromString(evt.orderId());
    var r = reservations.findById(orderId).orElse(null);
    if (r == null) return;

    if (r.getStatus() == StockStatus.RELEASED) return;

    if (r.getStatus() == StockStatus.RESERVED) {
      // release: restore inventory
      var inv = inventory.findById(r.getSku()).orElse(null);
      if (inv != null) {
        inv.setAvailableQty(inv.getAvailableQty() + r.getQty());
        inventory.save(inv);
      }
      r.setStatus(StockStatus.RELEASED);
      reservations.save(r);
    }
  }
}

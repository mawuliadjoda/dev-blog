package com.esprit.order.api;

import com.esprit.order.api.messages.ReservePaymentCommand;
import com.esprit.order.domain.*;
import com.esprit.order.infra.messaging.StreamPublisher;
import   jakarta.validation.Valid;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.Map;
import java.util.UUID;

@RequiredArgsConstructor
@RestController
@RequestMapping("/orders")
public class OrderController {

  private final OrderRepository orders;
  private final StreamPublisher publisher;

  @PostMapping
  public Map<String, Object> create(@RequestBody @Valid CreateOrderRequest req) {
    var id = UUID.randomUUID();

    var o = new OrderEntity();
    o.setId(id);
    o.setStatus(OrderStatus.PAYMENT_PENDING);
    o.setCompletedStep(SagaStep.NONE);
    o.setAmount(req.amount());
    o.setSku(req.sku());
    o.setQty(req.qty());
    orders.save(o);

    publisher.send("reservePaymentCommand-out-0",
        new ReservePaymentCommand(UUID.randomUUID().toString(), id.toString(), req.amount())
    );

    return Map.of("orderId", id.toString(), "status", o.getStatus().name());
  }

  @GetMapping("/{id}")
  public OrderEntity get(@PathVariable String id) {
    return orders.findById(UUID.fromString(id)).orElseThrow();
  }

  public record CreateOrderRequest(
    @NotNull BigDecimal amount,
    @NotBlank String sku,
    @Min(1) int qty
  ) {}
}

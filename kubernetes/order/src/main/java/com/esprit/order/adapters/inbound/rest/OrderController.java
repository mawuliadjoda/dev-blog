
package com.esprit.order.adapters.inbound.rest;


import com.esprit.order.adapters.inbound.rest.dto.PlaceOrderRequest;
import com.esprit.order.domain.model.CustomerOrder;
import com.esprit.order.domain.model.OrderItem;
import com.esprit.order.domain.service.OrderUseCases;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/v1/orders")
@RequiredArgsConstructor
public class OrderController {
    private final OrderUseCases useCases;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public CustomerOrder place(@Valid @RequestBody PlaceOrderRequest req) {
        return useCases.place(req.getCustomerEmail(),
                req.getItems().stream().map(i -> OrderItem.builder().productId(i.getProductId()).sku(i.getSku()).quantity(i.getQuantity()).price(i.getPrice()).build()).collect(Collectors.toList()));
    }
}

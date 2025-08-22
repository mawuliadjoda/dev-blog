
package com.esprit.order.adapters.inbound.rest.dto;

import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PlaceOrderRequest {
    @Email
    @NotBlank
    private String customerEmail;
    @NotEmpty
    private List<Item> items;

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    public static class Item {
        @NotBlank
        private String productId;
        @NotBlank
        private String sku;
        @Positive
        private int quantity;
        @PositiveOrZero
        private double price;
    }
}

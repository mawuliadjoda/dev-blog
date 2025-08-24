package com.esprit.product.adapters.inbound.rest.data.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@AllArgsConstructor
@Getter
@Setter
@Builder
public class ProductQueryResponse {
    private Long id;
    private String name;
    public double price;
    public int quantity;
}
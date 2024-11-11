package com.esprit.inventory.controller.data.request;

import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;

@Builder
@Getter
@Setter
@EqualsAndHashCode
public class CreateProductRequest {
    private String name;
    private String description;
    private double price;
    private int stockQuantity;
}
package com.esprit.order.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Builder
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class OrderItemResponse {
    private Long productId;    // Identifiant du produit
    private String productName; // Nom du produit
    private int quantity;      // Quantité commandée
    private double price;      // Prix unitaire du produit
}

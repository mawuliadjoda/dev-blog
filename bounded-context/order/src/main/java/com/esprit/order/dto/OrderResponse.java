package com.esprit.order.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

@Builder
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class OrderResponse {

    private Long orderId;             // Identifiant de la commande
    private LocalDateTime orderDate;         // Date de la commande
    private String status;            // Statut de la commande
    private List<OrderItemResponse> items; // Liste des articles de la commande avec les détails des produits
}


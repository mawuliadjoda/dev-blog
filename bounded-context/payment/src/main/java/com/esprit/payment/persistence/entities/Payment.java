package com.esprit.payment.persistence.entities;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;


@AllArgsConstructor
@NoArgsConstructor
@Builder
@Setter
@Getter

@Entity
public class Payment {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Long orderId;  // Référence à l'ID de la commande
    private double amount;
    private String paymentMethod;  // Ex: "Credit Card", "PayPal"
    private String status;  // Ex: "Completed", "Pending", "Failed"
    private LocalDateTime paymentDate;

    @OneToOne(mappedBy = "payment", cascade = CascadeType.ALL)
    private Invoice invoice;
}


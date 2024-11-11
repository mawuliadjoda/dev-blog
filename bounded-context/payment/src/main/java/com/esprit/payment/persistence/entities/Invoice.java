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
public class Invoice {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne
    @JoinColumn(name = "payment_id")
    private Payment payment;

    private double amount;
    private double tax;
    private double totalAmount;
    private LocalDateTime invoiceDate;

    private String invoiceNumber;
}

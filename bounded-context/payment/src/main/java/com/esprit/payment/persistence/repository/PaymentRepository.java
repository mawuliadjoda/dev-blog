package com.esprit.payment.persistence.repository;

import com.esprit.payment.persistence.entities.Payment;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PaymentRepository extends JpaRepository<Payment, Long> {
    // Possibilité de définir des méthodes personnalisées, par exemple pour trouver des paiements par statut ou par méthode de paiement
}


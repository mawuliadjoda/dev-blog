package com.esprit.payment.persistence.repository;

import com.esprit.payment.persistence.entities.Invoice;
import org.springframework.data.jpa.repository.JpaRepository;

public interface InvoiceRepository extends JpaRepository<Invoice, Long> {
    // Méthodes supplémentaires si besoin
}

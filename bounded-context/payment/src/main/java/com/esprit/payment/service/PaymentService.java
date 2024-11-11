package com.esprit.payment.service;

import com.esprit.payment.persistence.entities.Invoice;
import com.esprit.payment.persistence.entities.Payment;
import com.esprit.payment.persistence.repository.InvoiceRepository;
import com.esprit.payment.persistence.repository.PaymentRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class PaymentService {


    private final PaymentRepository paymentRepository;

    private final InvoiceRepository invoiceRepository;

    @Transactional
    public Payment processPayment(Long orderId, double amount, String paymentMethod) {
        // Création d'une nouvelle transaction de paiement
        Payment payment = new Payment();
        payment.setOrderId(orderId);
        payment.setAmount(amount);
        payment.setPaymentMethod(paymentMethod);
        payment.setStatus("Completed");
        payment.setPaymentDate(LocalDateTime.now());

        payment = paymentRepository.save(payment);

        // Génération de la facture associée
        Invoice invoice = generateInvoice(payment);
        invoiceRepository.save(invoice);

        payment.setInvoice(invoice); // Associer la facture au paiement
        return paymentRepository.save(payment); // Mettre à jour avec la facture liée
    }

    private Invoice generateInvoice(Payment payment) {
        Invoice invoice = new Invoice();
        invoice.setPayment(payment);
        invoice.setAmount(payment.getAmount());
        invoice.setTax(payment.getAmount() * 0.2);  // Ex: Calcul de la taxe (20%)
        invoice.setTotalAmount(payment.getAmount() + invoice.getTax());
        invoice.setInvoiceDate(LocalDateTime.now());
        invoice.setInvoiceNumber("INV-" + payment.getId() + "-" + LocalDateTime.now());

        return invoice;
    }
}

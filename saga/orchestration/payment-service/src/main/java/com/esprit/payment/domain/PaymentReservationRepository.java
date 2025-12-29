package com.esprit.payment.domain;

import org.springframework.data.jpa.repository.JpaRepository;
import java.util.UUID;

public interface PaymentReservationRepository extends JpaRepository<PaymentReservationEntity, UUID> {}

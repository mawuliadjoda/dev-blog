package com.esprit.stock.domain;

import org.springframework.data.jpa.repository.JpaRepository;
import java.util.UUID;

public interface StockReservationRepository extends JpaRepository<StockReservationEntity, UUID> {}

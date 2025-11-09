package com.esprit.domain.model;

import com.esprit.domain.model.common.BatchIdentifiable;
import lombok.Data;
import java.math.BigDecimal;
import java.time.LocalDate;

@Data
public class StgOrder implements BatchIdentifiable {
    private Long id;
    private Long customerId;
    private LocalDate orderDate;
    private BigDecimal amount;
    private String status;

    private String batchId;
}
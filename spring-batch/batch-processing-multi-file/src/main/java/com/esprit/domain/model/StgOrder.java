package com.esprit.domain.model;

import lombok.Data;
import java.math.BigDecimal;
import java.time.LocalDate;

@Data
public class StgOrder {
    private Long id;
    private Long customerId;
    private LocalDate orderDate;
    private BigDecimal amount;
    private String status;
}
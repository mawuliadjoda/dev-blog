package com.esprit.persistence.entities;

import com.esprit.persistence.entities.common.BatchIdentifiable;
import com.esprit.persistence.entities.compositekey.StgOrderId;
import jakarta.persistence.*;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDate;


@Entity
@Table(name = "stg_order")
@Data
@IdClass(StgOrderId.class)
public class StgOrderEntity implements BatchIdentifiable {

    //@Id
    //@GeneratedValue(strategy = GenerationType.IDENTITY)
    //private Long technicalId; // PK technique

    @Id
    @Column(name = "order_id", nullable = false)
    private Long id;          // id du CSV

    @Column(name = "customer_id", nullable = false)
    private Long customerId;

    private LocalDate orderDate;
    private BigDecimal amount;
    private String status;


    @Id
    @Column(name = "batch_id", nullable = false)
    private String batchId;
}
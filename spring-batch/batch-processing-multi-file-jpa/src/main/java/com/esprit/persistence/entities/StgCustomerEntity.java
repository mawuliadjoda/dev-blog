package com.esprit.persistence.entities;

import com.esprit.persistence.entities.common.BatchIdentifiable;
import com.esprit.persistence.entities.compositekey.StgCustomerId;
import com.esprit.persistence.entities.compositekey.StgOrderId;
import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalDate;

@Entity
@Table(name = "stg_customer")
@Data
@IdClass(StgCustomerId.class)
public class StgCustomerEntity implements BatchIdentifiable {

    //@Id
    //@GeneratedValue(strategy = GenerationType.IDENTITY)
    //private Long technicalId; // PK technique, pas exposée dans le CSV

    @Id
    @Column(name = "customer_id", nullable = false)
    private Long id;          // id du CSV

    @Column(name = "first_name")
    private String firstName;

    @Column(name = "last_name")
    private String lastName;

    private String email;
    private String gender;

    @Column(name = "contact")
    private String contactNo;

    private String country;
    private LocalDate dob;

    @Id
    @Column(name = "batch_id", nullable = false)
    private String batchId;
}
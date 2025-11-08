package com.esprit.domain.model;

import lombok.Data;
import java.time.LocalDate;

@Data
public class StgCustomer {
    private Long id;
    private String firstName;
    private String lastName;
    private String email;
    private String gender;
    private String contactNo;
    private String country;
    private LocalDate dob;

    private String batchId;
}
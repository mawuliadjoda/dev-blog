package com.esprit.domain.model;

import com.esprit.domain.model.common.BatchIdentifiable;
import lombok.Data;
import java.time.LocalDate;

@Data
public class StgCustomer implements BatchIdentifiable {
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
package com.esprit.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.OneToOne;
import lombok.Data;

@Data
@Entity
public class Bio {
    @Id
    @GeneratedValue
    private Long id;

    private String description;

    @OneToOne(mappedBy = "bio")
    private Author author;
}


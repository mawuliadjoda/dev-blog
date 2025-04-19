package com.ajiput.spring.data.rest.demo.entity;

import jakarta.persistence.*;
import lombok.Data;

@Entity
@Table(name = "persons")
@Data
public class Person {

    @Id
    private String identity;

    private String firstName;

    private String lastName;

    private String address;

    @Basic(optional = false)
    @Column(name = "department_code")
    private String departmentCode;

    @JoinColumn(name = "department_code", insertable = false, updatable = false)
    @ManyToOne
    private Department department;
}

package com.esprit.model;


import lombok.*;

@NoArgsConstructor
@AllArgsConstructor
@Builder
@Getter
@Setter
public class Person {
    private int id;
    private String name;
    private String role;
}
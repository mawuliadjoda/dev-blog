package com.esprit.entity;

import jakarta.persistence.*;
import lombok.Data;


import java.util.List;

@Data
@Entity
public class Author {
    @Id
    @GeneratedValue
    private Long id;

    private String name;

    @OneToMany(mappedBy = "author", fetch = FetchType.LAZY)
    private List<Book> books;
}

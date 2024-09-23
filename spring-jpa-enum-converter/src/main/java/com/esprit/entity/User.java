package com.esprit.entity;

import com.esprit.ennum.StatusEnum;
import com.esprit.ennum.converter.StatusConverter;
import jakarta.persistence.*;
import lombok.*;

@NoArgsConstructor
@AllArgsConstructor
@Builder
@Getter

@EqualsAndHashCode(onlyExplicitlyIncluded = true)
@Entity
@Table(name = "utilisateurs")
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @EqualsAndHashCode.Include
    private Long id;

    @Column(name = "name", length = 50)
    private String name;

    @Convert(converter = StatusConverter.class)
    @Column(columnDefinition = "char")
    private StatusEnum isVerified;
}

package com.adjoda.persistence.entities;


import jakarta.persistence.*;
import lombok.*;

import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Entity
public class Profile {
    @Id
    private String code;

    @Column(nullable = false)
    private String label;

    @ManyToMany
    @JoinTable(name = "profile_permission",
            joinColumns = @JoinColumn(name = "profile_code"),
            inverseJoinColumns = @JoinColumn(name = "permission_code"))
    private List<Permission> permissions;
}

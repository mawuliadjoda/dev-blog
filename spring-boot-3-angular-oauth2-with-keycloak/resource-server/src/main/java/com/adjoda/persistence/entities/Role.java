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
public class Role {
    @Id
    private String name;

    @ManyToMany
    @JoinTable(name = "role_profile",
            joinColumns = @JoinColumn(name = "role_name"),
            inverseJoinColumns = @JoinColumn(name = "profile_code"))
    private List<Profile> profiles;
}

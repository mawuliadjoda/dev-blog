package com.esprit.library.persistence.entity.projection;

import com.esprit.library.persistence.entity.Person;
import org.springframework.data.rest.core.config.Projection;

@Projection(name = "personProjection",  types = {Person.class})
public interface PersonProjection {
    Long getId();
    String getFirstName();
    String getLastName();
    String getEmail();
}

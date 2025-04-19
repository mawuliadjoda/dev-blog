package com.ajiput.spring.data.rest.demo.projection;

import com.ajiput.spring.data.rest.demo.entity.Department;
import com.ajiput.spring.data.rest.demo.entity.Person;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import org.springframework.data.rest.core.config.Projection;

@Projection(name = "person-view", types = Person.class)
public interface PersonView {

    String getIdentity();

    String getFirstName();

    String getLastName();

    Department getDepartment();
}

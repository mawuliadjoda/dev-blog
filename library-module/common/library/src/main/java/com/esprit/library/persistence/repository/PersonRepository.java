package com.esprit.library.persistence.repository;


import com.esprit.library.persistence.entity.Person;
import com.esprit.library.persistence.entity.projection.PersonProjection;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;
import org.springframework.data.rest.core.annotation.RestResource;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@RepositoryRestResource(
        path = "persons",
        collectionResourceRel = "persons",
        excerptProjection = PersonProjection.class
)
public interface PersonRepository extends JpaRepository<Person, Long> {

    // /persons/search/by-firstName?firstName=Mawuli
    @RestResource(path = "by-firstName", rel = "by-firstName")
    Optional<List<Person>> findByFirstName(String firstName);
}

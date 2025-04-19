package com.ajiput.spring.data.rest.demo.repository;

import com.ajiput.spring.data.rest.demo.entity.Department;
import com.ajiput.spring.data.rest.demo.entity.Person;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

@RepositoryRestResource(collectionResourceRel = "person", path = "person")
public interface PersonRepository extends CrudRepository<Person, String> {

}

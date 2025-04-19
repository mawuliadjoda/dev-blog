package com.ajiput.spring.data.rest.demo.repository;

import com.ajiput.spring.data.rest.demo.entity.Department;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

@RepositoryRestResource(collectionResourceRel = "department", path = "department")
public interface DepartmentRepository extends CrudRepository<Department, String> {

}

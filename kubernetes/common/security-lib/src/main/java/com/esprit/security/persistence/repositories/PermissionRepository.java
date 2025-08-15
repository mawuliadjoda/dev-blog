package com.esprit.security.persistence.repositories;

import com.esprit.security.persistence.entities.Permission;
import org.springframework.data.jpa.repository.JpaRepository;


public interface PermissionRepository extends JpaRepository<Permission, String> {
}

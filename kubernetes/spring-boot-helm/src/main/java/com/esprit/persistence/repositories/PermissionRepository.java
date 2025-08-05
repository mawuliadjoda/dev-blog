package com.esprit.persistence.repositories;

import com.esprit.persistence.entities.Permission;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PermissionRepository extends JpaRepository<Permission, String> {
}

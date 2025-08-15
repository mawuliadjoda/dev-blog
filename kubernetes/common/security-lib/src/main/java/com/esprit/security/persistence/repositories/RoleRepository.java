package com.esprit.security.persistence.repositories;


import com.esprit.security.persistence.entities.Permission;
import com.esprit.security.persistence.entities.Role;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;


public interface RoleRepository extends JpaRepository<Role, String> {

    @Query("""
                SELECT DISTINCT perm FROM Role r
                JOIN r.permissions perm
                WHERE r.name IN :roleNames
            """)
    List<Permission> findPermissionsByRoleNames(@Param("roleNames") List<String> roleNames);

}

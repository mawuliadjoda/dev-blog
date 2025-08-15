package com.esprit.security.persistence.repositories;


import com.esprit.security.persistence.entities.User;
import org.springframework.data.jpa.repository.JpaRepository;



public interface UserRepository extends JpaRepository<User, Long> {
}

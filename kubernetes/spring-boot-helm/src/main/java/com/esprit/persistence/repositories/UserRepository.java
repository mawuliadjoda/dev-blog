package com.esprit.persistence.repositories;

import com.esprit.persistence.entities.User;
import org.springframework.data.jpa.repository.JpaRepository;


public interface UserRepository extends JpaRepository<User, Long> {
}

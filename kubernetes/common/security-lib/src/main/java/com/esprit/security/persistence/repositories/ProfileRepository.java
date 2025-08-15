package com.esprit.security.persistence.repositories;


import com.esprit.security.persistence.entities.Profile;
import org.springframework.data.jpa.repository.JpaRepository;


public interface ProfileRepository extends JpaRepository<Profile, String> {
}

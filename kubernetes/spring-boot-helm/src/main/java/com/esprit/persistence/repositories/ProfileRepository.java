package com.esprit.persistence.repositories;


import com.esprit.persistence.entities.Profile;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ProfileRepository extends JpaRepository<Profile, String> {
}

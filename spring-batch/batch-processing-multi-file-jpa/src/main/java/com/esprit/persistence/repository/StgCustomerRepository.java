package com.esprit.persistence.repository;

import com.esprit.persistence.entities.StgCustomerEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface StgCustomerRepository extends JpaRepository<StgCustomerEntity, Long> {

    @Modifying
    @Query("delete from StgCustomerEntity")
    void truncate(); // delete all (TRUNCATE-like côté JPA)
}

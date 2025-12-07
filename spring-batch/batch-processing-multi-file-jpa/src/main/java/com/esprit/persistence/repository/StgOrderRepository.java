package com.esprit.persistence.repository;

import com.esprit.persistence.entities.StgOrderEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface StgOrderRepository extends JpaRepository<StgOrderEntity, Long> {

    @Modifying
    @Query("delete from StgOrderEntity ")
    void truncate();
}

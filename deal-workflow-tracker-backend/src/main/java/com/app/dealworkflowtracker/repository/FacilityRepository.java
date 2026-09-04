package com.app.dealworkflowtracker.repository;

import com.app.dealworkflowtracker.entities.Facility;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface FacilityRepository extends JpaRepository<Facility, Long> {

    // Find all facilities assigned to a specific bank entity
    List<Facility> findByBankEntityId(Long bankEntityId);
}
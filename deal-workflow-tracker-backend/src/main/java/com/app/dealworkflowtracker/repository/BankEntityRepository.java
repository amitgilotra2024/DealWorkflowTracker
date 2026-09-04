package com.app.dealworkflowtracker.repository;

import com.app.dealworkflowtracker.entities.BankEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface BankEntityRepository extends JpaRepository<BankEntity, Long> {

    // Find all entities belonging to a specific deal card
    List<BankEntity> findByDealCardId(Long dealCardId);

    // Find all subsidiary/child entities for a parent entity
    List<BankEntity> findByParentEntityId(Long parentEntityId);
}
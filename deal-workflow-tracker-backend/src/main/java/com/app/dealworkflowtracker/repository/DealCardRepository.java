package com.app.dealworkflowtracker.repository;

import com.app.dealworkflowtracker.entities.DealCard;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface DealCardRepository extends JpaRepository<DealCard, Long> {
    List<DealCard> findByStatus(String status);
}
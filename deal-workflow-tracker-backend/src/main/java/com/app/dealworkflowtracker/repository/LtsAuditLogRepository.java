package com.app.dealworkflowtracker.repository;

import com.app.dealworkflowtracker.entities.LtsAuditLog;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;

public interface LtsAuditLogRepository extends JpaRepository<LtsAuditLog, Long> {
    Optional<LtsAuditLog> findTopByDealCardIdOrderByIdDesc(Long dealCardId);
}
package com.app.dealworkflowtracker.service.Impl;

import com.app.dealworkflowtracker.dto.LtsCallbackRequest;
import com.app.dealworkflowtracker.entities.DealCard;
import com.app.dealworkflowtracker.entities.LtsAuditLog;
import com.app.dealworkflowtracker.repository.DealCardRepository;
import com.app.dealworkflowtracker.repository.LtsAuditLogRepository;
import com.app.dealworkflowtracker.service.LtsCallbackService;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class LtsCallbackServiceImpl implements LtsCallbackService {

    private final DealCardRepository dealCardRepository;
    private final LtsAuditLogRepository ltsAuditLogRepository;

    @Override
    @Transactional
    public void processCallback(LtsCallbackRequest callback) {
        // 1. Fetch DealCard entity
        DealCard dealCard = dealCardRepository.findById(callback.getDealCardId())
                .orElseThrow(() -> new EntityNotFoundException("DealCard not found for ID: " + callback.getDealCardId()));

        // 2. Fetch active audit log entry
        LtsAuditLog auditLog = ltsAuditLogRepository.findTopByDealCardIdOrderByIdDesc(callback.getDealCardId())
                .orElseThrow(() -> new EntityNotFoundException("Audit log not found for DealCard ID: " + callback.getDealCardId()));

        // 3. Update audit log details
        auditLog.setReceivedAt(LocalDateTime.now());
        auditLog.setResponsePayload(callback.toString());

        // 4. Update deal card and audit status based on callback outcome
        if ("SUCCESS".equalsIgnoreCase(callback.getStatus())) {
            dealCard.setLtsDealId(callback.getLtsDealId());
            dealCard.setStatus("DRAFT"); // Transition back from PENDING_LTS
            auditLog.setStatus("SUCCESS");
            auditLog.setLtsDealId(callback.getLtsDealId());
        } else {
            dealCard.setStatus("LTS_FAILED");
            auditLog.setStatus("FAILED");
        }

        dealCardRepository.save(dealCard);
        ltsAuditLogRepository.save(auditLog);
    }
}
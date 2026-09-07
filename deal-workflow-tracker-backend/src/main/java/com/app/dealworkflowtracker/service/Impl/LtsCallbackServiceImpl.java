package com.app.dealworkflowtracker.service.Impl;

import com.app.dealworkflowtracker.dto.LtsCallbackRequest;
import com.app.dealworkflowtracker.entities.DealCard;
import com.app.dealworkflowtracker.entities.LtsAuditLog;
import com.app.dealworkflowtracker.repository.DealCardRepository;
import com.app.dealworkflowtracker.repository.LtsAuditLogRepository;
import com.app.dealworkflowtracker.service.LtsCallbackService;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class LtsCallbackServiceImpl implements LtsCallbackService {

    private final DealCardRepository dealCardRepository;
    private final LtsAuditLogRepository ltsAuditLogRepository;
    private final ObjectMapper objectMapper; // Inject Jackson mapper

    @Override
    @Transactional
    @SneakyThrows
    public void processCallback(LtsCallbackRequest callback) {
        DealCard dealCard = dealCardRepository.findById(callback.getDealCardId())
                .orElseThrow(() -> new EntityNotFoundException("DealCard not found for ID: " + callback.getDealCardId()));

        LtsAuditLog auditLog = ltsAuditLogRepository.findTopByDealCardIdOrderByIdDesc(callback.getDealCardId())
                .orElseThrow(() -> new EntityNotFoundException("Audit log not found for DealCard ID: " + callback.getDealCardId()));

        // Convert callback DTO to formatted JSON string
        String responseJson = objectMapper.writeValueAsString(callback);

        auditLog.setReceivedAt(LocalDateTime.now());
        auditLog.setResponsePayload(responseJson); // Store callback response payload

        if ("SUCCESS".equalsIgnoreCase(callback.getStatus())) {
            dealCard.setLtsDealId(callback.getLtsDealId());
            dealCard.setStatus("DRAFT");
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
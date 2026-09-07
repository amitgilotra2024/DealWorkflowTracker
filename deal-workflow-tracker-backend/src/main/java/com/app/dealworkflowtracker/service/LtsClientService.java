package com.app.dealworkflowtracker.service;

import com.app.dealworkflowtracker.entities.LtsAuditLog;
import com.app.dealworkflowtracker.repository.LtsAuditLogRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.time.LocalDateTime;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class LtsClientService {

    private final RestTemplate restTemplate;
    private final LtsAuditLogRepository ltsAuditLogRepository;
    private final ObjectMapper objectMapper;

    @SneakyThrows
    public String callLtsAndGetDealId(Long dealCardId) {
        LocalDateTime sentAt = LocalDateTime.now();

        Map<String, Object> requestMap = Map.of("dealCardId", dealCardId);
        String requestJson = objectMapper.writeValueAsString(requestMap);

        LtsAuditLog auditLog = LtsAuditLog.builder()
                .dealCardId(dealCardId)
                .requestPayload(requestJson)
                .sentAt(sentAt)
                .status("PENDING")
                .build();

        // Initial save for audit log
        auditLog = ltsAuditLogRepository.save(auditLog);

        try {
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_JSON);
            HttpEntity<String> entity = new HttpEntity<>(requestJson, headers);

            // Call LTS API
            ResponseEntity<Map> response = restTemplate.postForEntity(
                    "http://localhost:8080/api/external/lts/create-deal",
                    entity,
                    Map.class
            );

            LocalDateTime receivedAt = LocalDateTime.now();
            String responseJson = objectMapper.writeValueAsString(response.getBody());
            String ltsDealId = response.getBody().get("ltsDealId").toString();

            // Update audit record on success
            auditLog.setLtsDealId(ltsDealId);
            auditLog.setResponsePayload(responseJson);
            auditLog.setReceivedAt(receivedAt);
            auditLog.setStatus("SUCCESS");
            ltsAuditLogRepository.save(auditLog);

            return ltsDealId;

        } catch (Exception e) {
            auditLog.setReceivedAt(LocalDateTime.now());
            auditLog.setResponsePayload("ERROR: " + e.getMessage());
            auditLog.setStatus("FAILED");
            ltsAuditLogRepository.save(auditLog);
            throw new RuntimeException("Failed to register LTS Deal", e);
        }
    }
}
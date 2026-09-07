package com.app.dealworkflowtracker.service.Impl;

import com.app.dealworkflowtracker.dto.DealCardCreateRequest;
import com.app.dealworkflowtracker.entities.BankEntity;
import com.app.dealworkflowtracker.entities.DealCard;
import com.app.dealworkflowtracker.entities.Facility;
import com.app.dealworkflowtracker.entities.LtsAuditLog;
import com.app.dealworkflowtracker.entities.User;
import com.app.dealworkflowtracker.repository.DealCardRepository;
import com.app.dealworkflowtracker.repository.LtsAuditLogRepository;
import com.app.dealworkflowtracker.repository.UserRepository;
import com.app.dealworkflowtracker.service.DealCardService;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.RestTemplate;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class DealCardServiceImpl implements DealCardService {

    private final DealCardRepository dealCardRepository;
    private final UserRepository userRepository;
    private final LtsAuditLogRepository ltsAuditLogRepository;
    private final RestTemplate restTemplate;
    private final ObjectMapper objectMapper;

    @Override
    @Transactional
    public DealCard createDealCard(DealCardCreateRequest request, String username) {
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new EntityNotFoundException("User not found: " + username));

        DealCard card = new DealCard();
        card.setDealName(request.getDealName());
        card.setDealType(request.getDealType());
        card.setStatus(request.getStatus() != null ? request.getStatus() : "DRAFT");
        card.setBorrowerName(request.getBorrowerName());
        card.setAmount(request.getAmount());
        card.setCreatedBy(user);

        if (request.getBankEntity() != null) {
            BankEntity bankEntity = new BankEntity();
            bankEntity.setEntityName(request.getBankEntity().getEntityName());
            bankEntity.setEntityType(request.getBankEntity().getEntityType());
            bankEntity.setCountry(request.getBankEntity().getCountry());

            if (request.getBankEntity().getFacilities() != null) {
                List<Facility> facilities = request.getBankEntity().getFacilities().stream()
                        .map(facilityDto -> {
                            Facility facility = new Facility();
                            facility.setFacilityName(facilityDto.getFacilityName());
                            facility.setFacilityType(facilityDto.getFacilityType());
                            facility.setLimitAmount(facilityDto.getLimitAmount());
                            facility.setBankEntity(bankEntity);
                            return facility;
                        })
                        .collect(Collectors.toList());

                bankEntity.setFacilities(facilities);
            }
            card.setBankEntity(bankEntity);
        }

        // Pure entity creation—LTS call is NOT triggered here
        return dealCardRepository.save(card);
    }

    @Override
    @Transactional
    public DealCard invokeLtsDealCreation(Long dealCardId) {
        DealCard dealCard = dealCardRepository.findById(dealCardId)
                .orElseThrow(() -> new EntityNotFoundException("DealCard not found with ID: " + dealCardId));

        if (dealCard.getLtsDealId() != null) {
            throw new IllegalStateException("LTS Deal ID already exists for DealCard ID: " + dealCardId);
        }

        // Update status and fire asynchronous submission
        dealCard.setStatus("PENDING_LTS");
        DealCard updatedCard = dealCardRepository.save(dealCard);

        triggerAsyncLtsCall(dealCardId);

        return updatedCard;
    }

    @SneakyThrows
    private void triggerAsyncLtsCall(Long dealCardId) {
        String requestJson = objectMapper.writeValueAsString(Map.of("dealCardId", dealCardId));

        LtsAuditLog auditLog = LtsAuditLog.builder()
                .dealCardId(dealCardId)
                .requestPayload(requestJson)
                .sentAt(LocalDateTime.now())
                .status("IN_PROGRESS")
                .build();

        auditLog = ltsAuditLogRepository.save(auditLog);

        try {
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_JSON);
            HttpEntity<String> entity = new HttpEntity<>(requestJson, headers);

            // Expect immediate HTTP 202 Accepted response from API Gateway / Mock LTS
            ResponseEntity<Map> response = restTemplate.postForEntity(
                    "http://localhost:8080/api/external/lts/create-deal-async",
                    entity,
                    Map.class
            );

            if (response.getStatusCode() != HttpStatus.ACCEPTED && response.getStatusCode() != HttpStatus.OK) {
                auditLog.setStatus("SUBMISSION_FAILED");
                ltsAuditLogRepository.save(auditLog);
                throw new RuntimeException("Gateway rejected LTS submission for ID: " + dealCardId);
            }

        } catch (Exception e) {
            auditLog.setStatus("SUBMISSION_FAILED");
            auditLog.setResponsePayload("ERROR: " + e.getMessage());
            ltsAuditLogRepository.save(auditLog);
            throw new RuntimeException("LTS invocation failed for DealCard ID: " + dealCardId, e);
        }
    }

    @Override
    public DealCard getDealCardById(Long id) {
        return dealCardRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("DealCard not found with ID: " + id));
    }

    @Override
    public List<DealCard> getAllDealCards() {
        return dealCardRepository.findAll();
    }
}
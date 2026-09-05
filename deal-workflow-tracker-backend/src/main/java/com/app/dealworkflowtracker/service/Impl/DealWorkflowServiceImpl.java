package com.app.dealworkflowtracker.service.impl;

import com.app.dealworkflowtracker.domain.DealEvent;
import com.app.dealworkflowtracker.domain.DealState;
import com.app.dealworkflowtracker.engine.DealStateTransitionEngine;
import com.app.dealworkflowtracker.entities.AuditLog;
import com.app.dealworkflowtracker.entities.DealCard;
import com.app.dealworkflowtracker.entities.User;
import com.app.dealworkflowtracker.entities.Workflow;
import com.app.dealworkflowtracker.repository.AuditLogRepository;
import com.app.dealworkflowtracker.repository.DealCardRepository;
import com.app.dealworkflowtracker.repository.UserRepository;
import com.app.dealworkflowtracker.service.DealWorkflowService;
import jakarta.persistence.EntityNotFoundException;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.time.OffsetDateTime;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class DealWorkflowServiceImpl implements DealWorkflowService {

    private final DealCardRepository dealCardRepository;
    private final AuditLogRepository auditLogRepository;
    private final UserRepository userRepository;
    private final DealStateTransitionEngine transitionEngine;

    @Override
    @Transactional
    public DealCard processWorkflowEvent(Long dealId, DealEvent event, Long userId, HttpServletRequest request) {
        DealCard dealCard = dealCardRepository.findById(dealId)
                .orElseThrow(() -> new EntityNotFoundException("DealCard not found with ID: " + dealId));

        User actingUser = userRepository.findById(userId)
                .orElseThrow(() -> new EntityNotFoundException("User not found with ID: " + userId));

        String oldStatus = dealCard.getStatus();

        DealState currentState = dealCard.toDomainState();
        DealState newState = transitionEngine.transition(currentState, event);

        dealCard.applyDomainState(newState);
        dealCard.setUpdatedBy(actingUser);

        Workflow workflowEntry = Workflow.builder()
                .dealCard(dealCard)
                .stepName(event.getClass().getSimpleName())
                .status(newState.name())
                .fromState(oldStatus)
                .toState(newState.name())
                .actionBy(userId)
                .actionDate(OffsetDateTime.now())
                .comments(extractNotes(event))
                .build();

        dealCard.getWorkflows().add(workflowEntry);

        DealCard updatedDeal = dealCardRepository.save(dealCard);

        AuditLog audit = AuditLog.builder()
                .action("STATE_TRANSITION")
                .entityName("DealCard")
                .entityId(dealId)
                .oldValue(oldStatus)
                .newValue(updatedDeal.getStatus())
                .changedBy(userId)
                .changedOn(LocalDateTime.now())
                .ipAddress(request != null ? request.getRemoteAddr() : "INTERNAL")
                .userAgent(request != null ? request.getHeader("User-Agent") : "SYSTEM")
                .build();

        auditLogRepository.save(audit);

        return updatedDeal;
    }

    private String extractNotes(DealEvent event) {
        if (event instanceof DealEvent.Approve approveEvent) {
            return approveEvent.approvalNotes();
        } else if (event instanceof DealEvent.Reject rejectEvent) {
            return rejectEvent.reason();
        }
        return null;
    }
}
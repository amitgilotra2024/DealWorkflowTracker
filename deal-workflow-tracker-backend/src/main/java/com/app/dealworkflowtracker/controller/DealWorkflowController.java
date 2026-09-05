package com.app.dealworkflowtracker.controller;

import com.app.dealworkflowtracker.domain.DealEvent;
import com.app.dealworkflowtracker.dto.DealCardResponse;
import com.app.dealworkflowtracker.entities.DealCard;
import com.app.dealworkflowtracker.entities.User;
import com.app.dealworkflowtracker.service.DealWorkflowService; // Injected interface
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/deal-workflows")
@RequiredArgsConstructor
public class DealWorkflowController {

    private final DealWorkflowService dealWorkflowService; // Injected interface instead of Impl

    // Transition: DRAFT -> UNDERWRITING
    @PostMapping("/{id}/submit")
    @PreAuthorize("hasAnyRole('ANALYST', 'ADMIN')")
    public ResponseEntity<DealCardResponse> submitForUnderwriting(@PathVariable Long id,
                                                                  Authentication auth,
                                                                  HttpServletRequest request) {
        String username = extractUsername(auth);
        Long userId = extractUserId(auth);

        DealEvent event = new DealEvent.SubmitForUnderwriting(username);
        DealCard updatedCard = dealWorkflowService.processWorkflowEvent(id, event, userId, request);

        return ResponseEntity.ok(DealCardResponse.fromEntity(updatedCard));
    }

    // Transition: UNDERWRITING -> COMPLIANCE_CHECK
    @PostMapping("/{id}/pass-underwriting")
    @PreAuthorize("hasAnyRole('ANALYST', 'ADMIN')")
    public ResponseEntity<DealCardResponse> passUnderwriting(@PathVariable Long id,
                                                             Authentication auth,
                                                             HttpServletRequest request) {
        String username = extractUsername(auth);
        Long userId = extractUserId(auth);

        DealEvent event = new DealEvent.PassUnderwriting(username);
        DealCard updatedCard = dealWorkflowService.processWorkflowEvent(id, event, userId, request);

        return ResponseEntity.ok(DealCardResponse.fromEntity(updatedCard));
    }

    // Transition: COMPLIANCE_CHECK -> APPROVED
    @PostMapping("/{id}/approve")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<DealCardResponse> approveDeal(@PathVariable Long id,
                                                        @RequestParam(defaultValue = "Approved by admin") String notes,
                                                        Authentication auth,
                                                        HttpServletRequest request) {
        String username = extractUsername(auth);
        Long userId = extractUserId(auth);

        DealEvent event = new DealEvent.Approve(username, notes);
        DealCard updatedCard = dealWorkflowService.processWorkflowEvent(id, event, userId, request);

        return ResponseEntity.ok(DealCardResponse.fromEntity(updatedCard));
    }

    // Transition: ANY STATE -> REJECTED
    @PostMapping("/{id}/reject")
    @PreAuthorize("hasAnyRole('ANALYST', 'ADMIN')")
    public ResponseEntity<DealCardResponse> rejectDeal(@PathVariable Long id,
                                                       @RequestParam(defaultValue = "Criteria not met") String reason,
                                                       Authentication auth,
                                                       HttpServletRequest request) {
        String username = extractUsername(auth);
        Long userId = extractUserId(auth);

        DealEvent event = new DealEvent.Reject(username, reason);
        DealCard updatedCard = dealWorkflowService.processWorkflowEvent(id, event, userId, request);

        return ResponseEntity.ok(DealCardResponse.fromEntity(updatedCard));
    }

    private String extractUsername(Authentication auth) {
        return (auth != null && auth.isAuthenticated()) ? auth.getName() : "admin_user";
    }

    private Long extractUserId(Authentication auth) {
        if (auth == null || !auth.isAuthenticated()) {
            return 1L; // Fallback to admin_user ID 1 for local testing
        }

        Object principal = auth.getPrincipal();
        if (principal instanceof User user) {
            return user.getId();
        }

        return 1L;
    }
}
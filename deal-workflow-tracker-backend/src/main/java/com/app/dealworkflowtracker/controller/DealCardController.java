package com.app.dealworkflowtracker.controller;

import com.app.dealworkflowtracker.dto.DealCardCreateRequest;
import com.app.dealworkflowtracker.dto.DealCardResponse;
import com.app.dealworkflowtracker.entities.DealCard;
import com.app.dealworkflowtracker.service.Impl.DealCardServiceImpl;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/deal-cards")
@RequiredArgsConstructor
public class DealCardController {

    private final DealCardServiceImpl dealCardServiceImpl;

    @PostMapping("/createDealCard")
    @PreAuthorize("hasAnyRole('ANALYST', 'ADMIN')")
    public ResponseEntity<DealCardResponse> createDealCard(@Valid @RequestBody DealCardCreateRequest request,
                                                           Authentication auth) {
        String username = extractUsername(auth);
        DealCard savedCard = dealCardServiceImpl.createDealCard(request, username);
        return ResponseEntity.status(HttpStatus.CREATED).body(DealCardResponse.fromEntity(savedCard));
    }

    @GetMapping("/{id}")
    public ResponseEntity<DealCardResponse> getDealCardById(@PathVariable Long id) {
        DealCard dealCard = dealCardServiceImpl.getDealCardById(id);
        return ResponseEntity.ok(DealCardResponse.fromEntity(dealCard));
    }

    @GetMapping
    public ResponseEntity<List<DealCardResponse>> getAllDealCards() {
        List<DealCardResponse> cards = dealCardServiceImpl.getAllDealCards()
                .stream()
                .map(DealCardResponse::fromEntity)
                .toList();
        return ResponseEntity.ok(cards);
    }

    private String extractUsername(Authentication auth) {
        return (auth != null && auth.isAuthenticated()) ? auth.getName() : "admin_user";
    }
}
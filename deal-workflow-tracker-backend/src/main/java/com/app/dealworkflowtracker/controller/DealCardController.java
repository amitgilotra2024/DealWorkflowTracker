package com.app.dealworkflowtracker.controller;

import com.app.dealworkflowtracker.dto.DealCardCreateRequest;
import com.app.dealworkflowtracker.entities.DealCard;
import com.app.dealworkflowtracker.service.DealCardService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/deal-cards")
@RequiredArgsConstructor
public class DealCardController {

    private final DealCardService dealCardService;

    @GetMapping("/getAll")
    @PreAuthorize("hasAnyRole('VIEWER', 'ANALYST', 'ADMIN')")
    public List<DealCard> getAll() {
        return dealCardService.findAll();
    }

    @PostMapping("/createDealCard")
    @PreAuthorize("hasAnyRole('ANALYST', 'ADMIN')")
    public DealCard createDealCard(@Valid @RequestBody DealCardCreateRequest request, Authentication authentication) {
        String currentUsername = authentication.getName();
        return dealCardService.createDealCard(request, currentUsername);
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public void delete(@PathVariable Long id) {
        dealCardService.delete(id);
    }
}

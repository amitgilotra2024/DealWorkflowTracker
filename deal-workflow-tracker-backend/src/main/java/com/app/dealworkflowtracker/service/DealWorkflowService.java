package com.app.dealworkflowtracker.service;

import com.app.dealworkflowtracker.domain.DealEvent;
import com.app.dealworkflowtracker.entities.DealCard;
import jakarta.servlet.http.HttpServletRequest;

public interface DealWorkflowService {
    DealCard processWorkflowEvent(Long dealCardId, DealEvent event, Long userId, HttpServletRequest request);
}
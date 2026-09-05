package com.app.dealworkflowtracker.service;

import com.app.dealworkflowtracker.dto.DealCardCreateRequest;
import com.app.dealworkflowtracker.entities.DealCard;
import java.util.List;

public interface DealCardService {
    DealCard createDealCard(DealCardCreateRequest request, String username);
    DealCard getDealCardById(Long id);
    List<DealCard> getAllDealCards();
}
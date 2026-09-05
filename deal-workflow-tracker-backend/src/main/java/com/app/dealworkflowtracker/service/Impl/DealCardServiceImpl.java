package com.app.dealworkflowtracker.service.Impl;

import com.app.dealworkflowtracker.dto.DealCardCreateRequest;
import com.app.dealworkflowtracker.entities.BankEntity;
import com.app.dealworkflowtracker.entities.DealCard;
import com.app.dealworkflowtracker.entities.Facility;
import com.app.dealworkflowtracker.entities.User;
import com.app.dealworkflowtracker.repository.DealCardRepository;
import com.app.dealworkflowtracker.repository.UserRepository;
import com.app.dealworkflowtracker.service.DealCardService;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class DealCardServiceImpl implements DealCardService {

    private final DealCardRepository dealCardRepository;
    private final UserRepository userRepository;

    @Override
    @Transactional
    public DealCard createDealCard(DealCardCreateRequest request, String username) {
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new EntityNotFoundException("User not found with username: " + username));

        // 1. Instantiate and map DealCard fields
        DealCard card = new DealCard();
        card.setDealName(request.getDealName());
        card.setDealType(request.getDealType());
        card.setStatus(request.getStatus() != null ? request.getStatus() : "DRAFT");
        card.setBorrowerName(request.getBorrowerName());
        card.setAmount(request.getAmount());
        card.setCreatedBy(user); // Inherited from BaseLog as Long

        // 2. Map nested BankEntity details
        if (request.getBankEntity() != null) {
            BankEntity bankEntity = new BankEntity();
            bankEntity.setEntityName(request.getBankEntity().getEntityName());
            bankEntity.setEntityType(request.getBankEntity().getEntityType());
            bankEntity.setCountry(request.getBankEntity().getCountry());

            // 3. Map nested Facilities
            if (request.getBankEntity().getFacilities() != null) {
                List<Facility> facilities = request.getBankEntity().getFacilities().stream()
                        .map(facilityDto -> {
                            Facility facility = new Facility();
                            facility.setFacilityName(facilityDto.getFacilityName());
                            facility.setFacilityType(facilityDto.getFacilityType());
                            facility.setLimitAmount(facilityDto.getLimitAmount());
                            facility.setBankEntity(bankEntity); // Establish bi-directional association
                            return facility;
                        })
                        .collect(Collectors.toList());

                bankEntity.setFacilities(facilities);
            }

            // Set bi-directional relationship via helper method on DealCard
            card.setBankEntity(bankEntity);
        }

        return dealCardRepository.save(card);
    }

    @Override
    public DealCard getDealCardById(Long id) {
        return dealCardRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Deal Card not found with ID: " + id));
    }

    @Override
    public List<DealCard> getAllDealCards() {
        return dealCardRepository.findAll();
    }
}
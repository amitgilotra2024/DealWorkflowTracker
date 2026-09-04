package com.app.dealworkflowtracker.service;

import com.app.dealworkflowtracker.dto.DealCardCreateRequest;
import com.app.dealworkflowtracker.entities.BankEntity;
import com.app.dealworkflowtracker.entities.DealCard;
import com.app.dealworkflowtracker.entities.Facility;
import com.app.dealworkflowtracker.entities.User;
import com.app.dealworkflowtracker.repository.DealCardRepository;
import com.app.dealworkflowtracker.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@Service
@RequiredArgsConstructor
public class DealCardService {

    private final DealCardRepository dealCardRepository;
    private final UserRepository userRepository; // Injected instance, not static

    @Transactional(readOnly = true)
    public List<DealCard> findAll() {
        return dealCardRepository.findAll();
    }

    @Transactional(readOnly = true)
    public DealCard findById(Long id) {
        return dealCardRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Deal card not found with ID: " + id));
    }

    @Transactional
    public DealCard createDealCard(DealCardCreateRequest request, String username) {
        // Fetch User entity instance from repository
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "User not found with username: " + username));

        DealCard dealCard = new DealCard();
        dealCard.setDealName(request.getDealName());
        dealCard.setDealType(request.getDealType());
        dealCard.setStatus(request.getStatus());
        dealCard.setBorrowerName(request.getBorrowerName());
        dealCard.setAmount(request.getAmount());
        dealCard.setCreatedBy(user); // Sets User object

        BankEntity bankEntity = new BankEntity();
        bankEntity.setEntityName(request.getBankEntity().getEntityName());
        bankEntity.setEntityType(request.getBankEntity().getEntityType());
        bankEntity.setCountry(request.getBankEntity().getCountry());
        bankEntity.setCreatedBy(user); // Sets User object

        if (request.getBankEntity().getFacilities() != null) {
            for (DealCardCreateRequest.FacilityDto facilityDto : request.getBankEntity().getFacilities()) {
                Facility facility = new Facility();
                facility.setFacilityName(facilityDto.getFacilityName());
                facility.setFacilityType(facilityDto.getFacilityType());
                facility.setLimitAmount(facilityDto.getLimitAmount());
                facility.setCreatedBy(user); // Sets User object

                bankEntity.addFacility(facility);
            }
        }

        bankEntity.setDealCard(dealCard);
        dealCard.setBankEntity(bankEntity);

        return dealCardRepository.save(dealCard);
    }

    @Transactional
    public void delete(Long id) {
        if (!dealCardRepository.existsById(id)) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Cannot delete. Deal card not found with ID: " + id);
        }
        dealCardRepository.deleteById(id);
    }
}
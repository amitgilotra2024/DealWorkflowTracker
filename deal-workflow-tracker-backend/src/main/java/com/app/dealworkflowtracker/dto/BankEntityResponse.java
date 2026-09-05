package com.app.dealworkflowtracker.dto;

import com.app.dealworkflowtracker.entities.BankEntity;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class BankEntityResponse {

    private Long id;
    private String entityName;
    private String entityType;
    private String country;
    private List<FacilityResponse> facilities;
    private String createdBy;
    private LocalDateTime createdOn;
    private String updatedBy;
    private LocalDateTime updatedOn;

    public static BankEntityResponse fromEntity(BankEntity bankEntity) {
        if (bankEntity == null) return null;

        List<FacilityResponse> facilityResponses = bankEntity.getFacilities() != null ?
                bankEntity.getFacilities().stream()
                        .map(FacilityResponse::fromEntity)
                        .toList() : Collections.emptyList();

        return BankEntityResponse.builder()
                .id(bankEntity.getId())
                .entityName(bankEntity.getEntityName())
                .entityType(bankEntity.getEntityType())
                .country(bankEntity.getCountry())
                .facilities(facilityResponses)
                .createdBy(bankEntity.getCreatedBy() != null ? bankEntity.getCreatedBy().getUsername() : null)
                .createdOn(bankEntity.getCreatedOn())
                .updatedBy(bankEntity.getUpdatedBy() != null ? bankEntity.getUpdatedBy().getUsername() : null)
                .updatedOn(bankEntity.getUpdatedOn())
                .build();
    }
}
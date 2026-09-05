package com.app.dealworkflowtracker.dto;

import com.app.dealworkflowtracker.entities.Facility;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class FacilityResponse {

    private Long id;
    private String facilityName;
    private String facilityType;
    private Double limitAmount;
    private String createdBy;
    private LocalDateTime createdOn;
    private String updatedBy;
    private LocalDateTime updatedOn;

    public static FacilityResponse fromEntity(Facility facility) {
        if (facility == null) return null;

        return FacilityResponse.builder()
                .id(facility.getId())
                .facilityName(facility.getFacilityName())
                .facilityType(facility.getFacilityType())
                .limitAmount(facility.getLimitAmount())
                .createdBy(facility.getCreatedBy() != null ? facility.getCreatedBy().getUsername() : null)
                .createdOn(facility.getCreatedOn())
                .updatedBy(facility.getUpdatedBy() != null ? facility.getUpdatedBy().getUsername() : null)
                .updatedOn(facility.getUpdatedOn())
                .build();
    }
}
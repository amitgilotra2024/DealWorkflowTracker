package com.app.dealworkflowtracker.dto;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class DealCardCreateRequest {

    @NotBlank(message = "Deal name is required")
    private String dealName;

    private String dealType; // CREDIT, RENEWAL, etc.
    private String status;   // DRAFT, IN_PROGRESS, etc.
    private String borrowerName;

    @NotNull(message = "Amount is required")
    @Positive(message = "Amount must be greater than zero")
    private Double amount;

    // Nested BankEntity DTO
    @NotNull(message = "Bank entity details are required")
    @Valid
    private BankEntityDto bankEntity;

    // Nested DTO classes for structured binding
    @Getter
    @Setter
    public static class BankEntityDto {

        @NotBlank(message = "Entity name is required")
        private String entityName;

        private String entityType; // BORROWER, GUARANTOR, SUBSIDIARY, etc.
        private String country;

        @NotNull(message = "At least one facility is required")
        @Valid
        private List<FacilityDto> facilities;
    }

    @Getter
    @Setter
    public static class FacilityDto {

        @NotBlank(message = "Facility name is required")
        private String facilityName;

        private String facilityType;

        @NotNull(message = "Facility limit amount is required")
        @Positive(message = "Limit amount must be greater than zero")
        private Double limitAmount;
    }
}
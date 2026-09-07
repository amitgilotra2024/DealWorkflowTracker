package com.app.dealworkflowtracker.dto;

import com.app.dealworkflowtracker.entities.DealCard;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class DealCardResponse {

    private Long id;
    private String ltsDealId;
    private String dealName;
    private String dealType;
    private String status;
    private String borrowerName;
    private Double amount;
    private BankEntityResponse bankEntity;
    private String createdBy;
    private LocalDateTime createdOn;
    private String updatedBy;
    private LocalDateTime updatedOn;

    public static DealCardResponse fromEntity(DealCard entity) {
        if (entity == null) return null;

        return DealCardResponse.builder()
                .id(entity.getId())
                .ltsDealId(entity.getLtsDealId())
                .dealName(entity.getDealName())
                .dealType(entity.getDealType())
                .status(entity.getStatus())
                .borrowerName(entity.getBorrowerName())
                .amount(entity.getAmount())
                .bankEntity(BankEntityResponse.fromEntity(entity.getBankEntity()))
                .createdBy(extractUsername(entity.getCreatedBy()))
                .createdOn(entity.getCreatedOn())
                .updatedBy(extractUsername(entity.getUpdatedBy()))
                .updatedOn(entity.getUpdatedOn())
                .build();
    }

    /**
     * Safely extracts the username whether BaseLog stores a User object or String.
     */
    private static String extractUsername(Object userField) {
        return switch (userField) {
            case null -> null;
            case String username -> username;
            case com.app.dealworkflowtracker.entities.User user -> user.getUsername();
            default -> userField.toString();
        };
    }
}
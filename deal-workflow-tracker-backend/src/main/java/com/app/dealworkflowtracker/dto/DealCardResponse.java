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
                .dealName(entity.getDealName())
                .dealType(entity.getDealType())
                .status(entity.getStatus())
                .borrowerName(entity.getBorrowerName())
                .amount(entity.getAmount())
                .bankEntity(BankEntityResponse.fromEntity(entity.getBankEntity()))
                .createdBy(entity.getCreatedBy() != null ? entity.getCreatedBy().getUsername() : null)
                .createdOn(entity.getCreatedOn())
                .updatedBy(entity.getUpdatedBy() != null ? entity.getUpdatedBy().getUsername() : null)
                .updatedOn(entity.getUpdatedOn())
                .build();
    }
}
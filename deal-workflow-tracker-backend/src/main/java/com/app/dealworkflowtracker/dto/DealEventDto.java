package com.app.dealworkflowtracker.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class DealEventDto {
    private Long dealId;
    private String dealName;
    private String status;
    private String eventType; // e.g., "DEAL_CREATED", "DEAL_UPDATED"
    private String triggeredBy;
    private LocalDateTime timestamp;
}
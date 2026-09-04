package com.app.dealworkflowtracker.entities;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "outbox")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Outbox {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String aggregateType;          // DealCard, Workflow, Entity, etc.
    private Long aggregateId;

    private String eventType;              // DEAL_APPROVED, WORKFLOW_COMPLETED, ENTITY_ADDED, etc.

    @Column(columnDefinition = "TEXT")
    private String payload;                // JSON payload for the email/notification

    private String status = "PENDING";     // PENDING, SENT, FAILED

    private Integer retryCount = 0;

    private LocalDateTime createdOn = LocalDateTime.now();
    private LocalDateTime processedOn;

    private String errorMessage;
}
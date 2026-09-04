package com.app.dealworkflowtracker.entities;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "audit_logs")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class AuditLog {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String entityName;             // e.g. "DealCard", "Facility", "Entity"
    private Long entityId;

    private String action;                 // CREATE, UPDATE, DELETE, STATUS_CHANGE

    @Column(columnDefinition = "TEXT")
    private String oldValue;               // JSON or plain text

    @Column(columnDefinition = "TEXT")
    private String newValue;               // JSON or plain text

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "changed_by")
    private User changedBy;

    private LocalDateTime changedOn = LocalDateTime.now();

    private String ipAddress;
    private String userAgent;
}
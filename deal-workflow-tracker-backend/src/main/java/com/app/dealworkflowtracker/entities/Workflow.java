package com.app.dealworkflowtracker.entities;

import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDateTime;
@Entity
@Table(name = "workflows")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Workflow {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String stepName;
    private String status;            // PENDING, COMPLETED, REJECTED
    private String comments;
    private LocalDateTime actionDate = LocalDateTime.now();
    @ManyToOne
    @JoinColumn(name = "deal_card_id")
    private DealCard dealCard;
    @ManyToOne
    @JoinColumn(name = "action_by")
    private User actionBy;
}
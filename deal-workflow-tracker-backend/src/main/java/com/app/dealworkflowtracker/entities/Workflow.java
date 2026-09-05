package com.app.dealworkflowtracker.entities;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import lombok.*;

import java.time.OffsetDateTime;
import java.util.Objects;

@Entity
@Table(name = "workflows", schema = "public")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Workflow {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "step_name", nullable = false)
    private String stepName;

    @Column(nullable = false)
    private String status;

    @Column(name = "from_state")
    private String fromState;

    @Column(name = "to_state")
    private String toState;

    @Column(columnDefinition = "TEXT")
    private String comments;

    @Column(name = "action_by")
    private Long actionBy;

    @Column(name = "action_date", nullable = false, updatable = false)
    private OffsetDateTime actionDate;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "deal_card_id", nullable = false)
    @JsonIgnoreProperties("workflows")
    private DealCard dealCard;

    @PrePersist
    protected void onCreate() {
        if (this.actionDate == null) {
            this.actionDate = OffsetDateTime.now();
        }
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Workflow workflow = (Workflow) o;
        return id != null && Objects.equals(id, workflow.id);
    }

    @Override
    public int hashCode() {
        return getClass().hashCode();
    }
}
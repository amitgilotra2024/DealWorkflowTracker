package com.app.dealworkflowtracker.entities;

import com.app.dealworkflowtracker.domain.DealState;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import lombok.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Entity
@Table(name = "deal_card")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class DealCard extends BaseLog {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String dealName;
    private String dealType;
    private String status; // Persists the state string (e.g., "UNDERWRITING")
    private String borrowerName;
    private Double amount;

    // Domain tracking fields for state details
    private String lastUpdatedBy;
    private String statusNotes;

    // Shifted ownership: mappedBy points to the dealCard field in BankEntity
    @OneToOne(mappedBy = "dealCard", cascade = CascadeType.ALL, fetch = FetchType.EAGER, orphanRemoval = true)
    @JsonIgnoreProperties("dealCard")
    private BankEntity bankEntity;

    @OneToMany(mappedBy = "dealCard", cascade = CascadeType.ALL, orphanRemoval = true)
    @JsonIgnore
    private List<Workflow> workflows = new ArrayList<>();

    // --- State Engine Conversion Helper Methods ---

    /**
     * Converts persistent entity fields into a Java 21 Sealed State Record.
     */
    public DealState toDomainState() {
        return DealState.fromString(this.status, this.lastUpdatedBy, this.statusNotes);
    }

    /**
     * Updates entity fields based on the new Java 21 Sealed State Record.
     */
    public void applyDomainState(DealState newState) {
        this.status = newState.name();
        switch (newState) {
            case DealState.UnderwritingState u -> this.lastUpdatedBy = u.assignedAnalyst();
            case DealState.ComplianceCheckState c -> this.lastUpdatedBy = c.analyst();
            case DealState.ApprovedState a -> {
                this.lastUpdatedBy = a.approvedBy();
                this.statusNotes = a.approvalNotes();
            }
            case DealState.RejectedState r -> {
                this.lastUpdatedBy = r.rejectedBy();
                this.statusNotes = r.reason();
            }
            case DealState.DraftState d -> {}
        }
    }

    // --- Bi-directional Helper Methods ---

    public void setBankEntity(BankEntity bankEntity) {
        this.bankEntity = bankEntity;
        if (bankEntity != null) {
            bankEntity.setDealCard(this);
        }
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        DealCard dealCard = (DealCard) o;
        return id != null && Objects.equals(id, dealCard.id);
    }

    @Override
    public int hashCode() {
        return getClass().hashCode();
    }
}
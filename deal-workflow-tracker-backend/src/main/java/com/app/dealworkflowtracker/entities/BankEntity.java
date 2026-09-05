package com.app.dealworkflowtracker.entities;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import lombok.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Entity
@Table(name = "bank_entities")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class BankEntity extends BaseLog {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // Shifted ownership: BankEntity now holds the deal_card_id JoinColumn
    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "deal_card_id", referencedColumnName = "id")
    @JsonIgnoreProperties("bankEntity")
    private DealCard dealCard;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "entity_id")
    @JsonIgnoreProperties({"parentEntity", "facilities", "dealCard"})
    private BankEntity parentEntity;

    private String entityName;
    private String entityType;
    private String country;

    @OneToMany(mappedBy = "bankEntity", cascade = CascadeType.ALL, orphanRemoval = true)
    @JsonIgnoreProperties("bankEntity")
    private List<Facility> facilities = new ArrayList<>();

    // --- Bi-directional Helper Methods ---

    public void addFacility(Facility facility) {
        facilities.add(facility);
        facility.setBankEntity(this);
    }

    public void removeFacility(Facility facility) {
        facilities.remove(facility);
        facility.setBankEntity(null);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        BankEntity that = (BankEntity) o;
        return id != null && Objects.equals(id, that.id);
    }

    @Override
    public int hashCode() {
        return getClass().hashCode();
    }
}
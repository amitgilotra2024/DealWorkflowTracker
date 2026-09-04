package com.app.dealworkflowtracker.entities;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import lombok.*;

import java.util.ArrayList;
import java.util.List;

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

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "deal_card_id", unique = true)
    @JsonIgnoreProperties("bankEntity") // Prevents circular reference back to DealCard
    private DealCard dealCard;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "entity_id")
    @JsonIgnore
    private BankEntity parentEntity;

    private String entityName;
    private String entityType;
    private String country;

    @OneToMany(mappedBy = "bankEntity", cascade = CascadeType.ALL, orphanRemoval = true)
    @JsonIgnoreProperties("bankEntity") // Prevents circular reference from Facility
    private List<Facility> facilities = new ArrayList<>();

    public void addFacility(Facility facility) {
        facilities.add(facility);
        facility.setBankEntity(this);
    }
}
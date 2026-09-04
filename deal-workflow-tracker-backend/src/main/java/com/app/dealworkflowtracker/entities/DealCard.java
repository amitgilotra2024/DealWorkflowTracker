package com.app.dealworkflowtracker.entities;

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
    private String status;
    private String borrowerName;
    private Double amount;

    @OneToOne(mappedBy = "dealCard", cascade = CascadeType.ALL, fetch = FetchType.LAZY, orphanRemoval = true)
    @JsonIgnoreProperties("dealCard")
    private BankEntity bankEntity;

    @OneToMany(mappedBy = "dealCard", cascade = CascadeType.ALL, orphanRemoval = true)
    @JsonIgnore
    private List<Workflow> workflows = new ArrayList<>();

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
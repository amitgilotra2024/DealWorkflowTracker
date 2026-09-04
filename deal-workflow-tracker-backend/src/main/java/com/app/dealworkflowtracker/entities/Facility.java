package com.app.dealworkflowtracker.entities;

import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;
import lombok.*;

import java.util.Objects;

@Entity
@Table(name = "facilities")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Facility extends BaseLog {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String facilityName;
    private String facilityType;
    private Double limitAmount;

    // Point to entity_id to match existing PostgreSQL table column
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "entity_id", nullable = false)
    @JsonBackReference // Prevents Jackson from serializing back to BankEntity endlessly
    private BankEntity bankEntity;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Facility facility = (Facility) o;
        return id != null && Objects.equals(id, facility.id);
    }

    @Override
    public int hashCode() {
        return getClass().hashCode();
    }
}
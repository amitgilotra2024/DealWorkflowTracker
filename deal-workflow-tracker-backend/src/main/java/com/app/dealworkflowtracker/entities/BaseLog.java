package com.app.dealworkflowtracker.entities;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import java.time.LocalDateTime;
@MappedSuperclass
@Getter
@Setter
public abstract class BaseLog {

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "created_by")
    @JsonIgnoreProperties({"password", "roles", "hibernateLazyInitializer", "handler"})
    private User createdBy;

    @Column(name = "created_on", nullable = false, updatable = false)
    private LocalDateTime createdOn = LocalDateTime.now();

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "updated_by")
    @JsonIgnoreProperties({"password", "roles", "hibernateLazyInitializer", "handler"})
    private User updatedBy;

    @Column(name = "updated_on")
    private LocalDateTime updatedOn;
}
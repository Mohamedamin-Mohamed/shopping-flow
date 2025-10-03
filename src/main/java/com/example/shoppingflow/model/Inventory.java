package com.example.shoppingflow.model;

import com.example.shoppingflow.DTO.InventoryId;
import jakarta.persistence.*;

import java.time.Instant;

@Entity(name = "inventory")
public class Inventory {
    @EmbeddedId
    private InventoryId id;

    @Column(name = "tsin", unique = true)
    private String tsin;

    private long totalQuantity;
    private long reservedQuantity;

    @Transient
    private long getAvailableQuantity() {
        return totalQuantity - reservedQuantity;
    }

    private Instant lastUpdated;

    @PrePersist
    @PreUpdate
    void onCreate() {
        this.lastUpdated = Instant.now();
    }
}

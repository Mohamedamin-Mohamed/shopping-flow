package com.example.shoppingflow.model;

import com.example.shoppingflow.DTO.InventoryId;
import jakarta.persistence.*;

import java.time.Instant;

@Entity(name = "inventory")
public class Inventory {
    public InventoryId getInventoryId() {
        return inventoryId;
    }

    @EmbeddedId
    private InventoryId inventoryId;

    public long getReservedQuantity() {
        return reservedQuantity;
    }

    public void setReservedQuantity(long reservedQuantity) {
        this.reservedQuantity = reservedQuantity;
    }

    private long totalQuantity;

    public void setTotalQuantity(long totalQuantity) {
        this.totalQuantity = totalQuantity;
    }

    public long getTotalQuantity() {
        return totalQuantity;
    }

    public long reservedQuantity;

    @Transient
    public long getAvailableQuantity() {
        return totalQuantity - reservedQuantity;
    }

    private Instant lastUpdated;

    @PrePersist
    @PreUpdate
    void onCreate() {
        this.lastUpdated = Instant.now();
    }
}

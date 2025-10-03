package com.example.shoppingflow.DTO;

import jakarta.persistence.Embeddable;

import java.io.Serializable;

@Embeddable
public class InventoryId implements Serializable {
    private long StoreId;
    private String tsin;
}

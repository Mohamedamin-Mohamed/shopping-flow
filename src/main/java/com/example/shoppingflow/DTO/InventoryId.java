package com.example.shoppingflow.DTO;

import jakarta.persistence.Embeddable;

import java.io.Serializable;

@Embeddable
public class InventoryId implements Serializable {
    private String storeId;
    private String tsin;

    public void setStoreId(String storeId){
        this.storeId = storeId;
    }

    public void setTsin(String tsin){
        this.tsin = tsin;
    }

    public String getStoreId() {
        return storeId;
    }

    public String getTsin() {
        return tsin;
    }

}

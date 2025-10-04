package com.example.shoppingflow.DTO;

public class InventoryInfo {
    private String storeId;
    private String tsin;
    private long totalQuantity;
    private long reservedQuantity;

    public void setStoreId(String storeId) {
        this.storeId = storeId;
    }

    public void setTsin(String tsin) {
        this.tsin = tsin;
    }

    public void setTotalQuantity(long totalQuantity) {
        this.totalQuantity = totalQuantity;
    }

    public void setReservedQuantity(long reservedQuantity) {
        this.reservedQuantity = reservedQuantity;
    }

    public String getStoreId() {
        return storeId;
    }

    public String getTsin() {
        return tsin;
    }

    public long getTotalQuantity() {
        return totalQuantity;
    }

    public long getReservedQuantity() {
        return reservedQuantity;
    }

}

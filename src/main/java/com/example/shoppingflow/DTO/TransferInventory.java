package com.example.shoppingflow.DTO;

public record TransferInventory(String sendingStoreId, String receivingStoreID, String tsin, long quantity) {
}

package com.example.shoppingflow.DTO;

import com.example.shoppingflow.model.Inventory;

public record InventoryTransferResponse(Inventory sendingStoreInventory, Inventory receivingStoreInventory) {
}

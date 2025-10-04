package com.example.shoppingflow.service;

import com.example.shoppingflow.DTO.InventoryId;
import com.example.shoppingflow.DTO.InventoryInfo;
import com.example.shoppingflow.model.Inventory;
import com.example.shoppingflow.repository.InventoryRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.concurrent.CompletableFuture;

@Service
public class InventoryService {
    private final InventoryRepository inventoryRepository;

    public InventoryService(InventoryRepository inventoryRepository) {
        this.inventoryRepository = inventoryRepository;
    }

    public CompletableFuture<Inventory> addInventory(InventoryInfo inventoryInfo) {
        return inventoryRepository.addInventory(inventoryInfo);
    }

    public CompletableFuture<Inventory> findInventory(InventoryId inventoryId) {
        return inventoryRepository.findInventory(inventoryId);
    }

    public CompletableFuture<List<Inventory>> findInventoryByStoreId(String storeId) {
        return inventoryRepository.findInventoryByStoreId(storeId);
    }

    public CompletableFuture<List<Inventory>> findInventoryByTsin(String tsin) {
        return inventoryRepository.findInventoryByTsin(tsin);
    }

    public CompletableFuture<Inventory> reserveInventory(String storeId, String tsin, long quantityToReserve) {
        InventoryId inventoryId = buildInventoryId(storeId, tsin);

        return inventoryRepository.findInventory(inventoryId).thenCompose(inventory -> {
            long availableQuantity = inventory.getAvailableQuantity();
            if (availableQuantity < quantityToReserve) {
                throw new IllegalArgumentException("Not enough stock: available=" + availableQuantity + ", requested=" + quantityToReserve);
            }

            long reservedQuantity = inventory.reservedQuantity;
            inventory.setReservedQuantity(reservedQuantity + quantityToReserve);
            return addInventory(convertToInventoryInfo(inventory));
        });
    }

    public CompletableFuture<Inventory> unReserveInventory(String storeId, String tsin, long quantityToUnreserve) {
        InventoryId inventoryId = buildInventoryId(storeId, tsin);

        return inventoryRepository.findInventory(inventoryId).thenCompose(inventory -> {
            long reservedQuantity = inventory.reservedQuantity;
            if (quantityToUnreserve > reservedQuantity) {
                throw new IllegalArgumentException("Cannot unreserve more than currently reserved: reserved=" + inventory.getReservedQuantity() + ", requested=" + quantityToUnreserve);
            }

            inventory.setReservedQuantity(reservedQuantity - quantityToUnreserve);
            return addInventory(convertToInventoryInfo(inventory));
        });
    }

    private InventoryId buildInventoryId(String storeId, String tsin) {
        InventoryId inventoryId = new InventoryId();
        inventoryId.setStoreId(storeId);
        inventoryId.setTsin(tsin);

        return inventoryId;
    }

    private InventoryInfo convertToInventoryInfo(Inventory inventory) {
        InventoryInfo inventoryInfo = new InventoryInfo();
        inventoryInfo.setStoreId(inventory.getInventoryId().getStoreId());
        inventoryInfo.setTsin(inventory.getInventoryId().getTsin());
        inventoryInfo.setTotalQuantity(inventory.getTotalQuantity());
        inventoryInfo.setReservedQuantity(inventory.reservedQuantity);

        return inventoryInfo;
    }
}

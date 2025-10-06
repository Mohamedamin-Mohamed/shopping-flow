package com.example.shoppingflow.service;

import com.example.shoppingflow.DTO.InventoryId;
import com.example.shoppingflow.DTO.InventoryInfo;
import com.example.shoppingflow.DTO.InventoryTransferResponse;
import com.example.shoppingflow.DTO.TransferInventory;
import com.example.shoppingflow.model.Inventory;
import com.example.shoppingflow.repository.InventoryRepository;
import com.example.shoppingflow.repository.OrderRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.concurrent.CompletableFuture;

@Service
public class InventoryService {
    private final InventoryRepository inventoryRepository;

    public InventoryService(InventoryRepository inventoryRepository, OrderRepository orderRepository) {
        this.inventoryRepository = inventoryRepository;
    }

    public CompletableFuture<Inventory> addInventory(InventoryInfo inventoryInfo) {
        return inventoryRepository.addInventory(inventoryInfo);
    }

    public CompletableFuture<Inventory> findInventory(String storeId, String tsin) {
        return inventoryRepository.findInventory(buildInventoryId(storeId, tsin));
    }

    public CompletableFuture<List<Inventory>> findInventoryByStoreId(String storeId) {
        return inventoryRepository.findInventoryByStoreId(storeId);
    }

    public CompletableFuture<List<Inventory>> findInventoryByTsin(String tsin) {
        return inventoryRepository.findInventoryByTsin(tsin);
    }

    public CompletableFuture<Inventory> reserveInventory(String storeId, String tsin, long quantityToReserve) {
        return findInventoryHelper(buildInventoryId(storeId, tsin)).thenCompose(inventory -> {
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
        return findInventoryHelper(buildInventoryId(storeId, tsin)).thenCompose(inventory -> {
            long reservedQuantity = inventory.reservedQuantity;

            if (quantityToUnreserve > reservedQuantity) {
                throw new IllegalArgumentException("Cannot unreserve more than currently reserved: reserved=" + inventory.getReservedQuantity() + ", requested=" + quantityToUnreserve);
            }

            inventory.setReservedQuantity(reservedQuantity - quantityToUnreserve);
            return addInventory(convertToInventoryInfo(inventory));
        });
    }

    public CompletableFuture<Inventory> confirmPurchase(String storeId, String tsin, long quantityToPurchase) {
        return findInventoryHelper(buildInventoryId(storeId, tsin)).thenCompose(inventory -> {
            long totalQuantity = inventory.getTotalQuantity();
            long reservedQuantity = inventory.getReservedQuantity();

            if (quantityToPurchase > totalQuantity) {
                throw new IllegalArgumentException("Cannot purchase more than total quantity: total=" + totalQuantity + ", requested=" + quantityToPurchase);
            }
            if (quantityToPurchase > reservedQuantity) {
                throw new IllegalArgumentException("Cannot purchase more than currently reserved: reserved=" + reservedQuantity + ", requested=" + quantityToPurchase);
            }

            inventory.setTotalQuantity(totalQuantity - quantityToPurchase);
            inventory.setReservedQuantity(reservedQuantity - quantityToPurchase);
            return addInventory(convertToInventoryInfo(inventory));
        });
    }

    public CompletableFuture<Inventory> restockItem(String storeId, String tsin, long quantityToRestock) {
        return findInventoryHelper(buildInventoryId(storeId, tsin)).thenCompose(inventory -> {
            long totalQuantity = inventory.getTotalQuantity();

            if (quantityToRestock < 0) {
                throw new IllegalArgumentException("Not enough units to restock: total=" + quantityToRestock + ", quantity to restock=" + quantityToRestock);
            }

            inventory.setTotalQuantity(totalQuantity + quantityToRestock);
            return addInventory(convertToInventoryInfo(inventory));
        });
    }

    public CompletableFuture<InventoryTransferResponse> transferInventory(TransferInventory transferInventory) {
        //compute inventory adjustment for both stores and return the updated inventory
        CompletableFuture<Inventory> sendingStoreInventory = findInventoryHelper(buildInventoryId(transferInventory.sendingStoreId(), transferInventory.tsin()))
                .thenCompose(inventory -> {
                    long totalQuantity = inventory.getTotalQuantity();
                    if (transferInventory.quantity() > totalQuantity) {
                        throw new IllegalArgumentException("Not enough units to transfer: total=" + totalQuantity + ", quantity to transfer=" + transferInventory.quantity());
                    }
                    inventory.setTotalQuantity(totalQuantity - transferInventory.quantity());
                    return addInventory(convertToInventoryInfo(inventory));
                });

        CompletableFuture<Inventory> receivingStoreInventory = findInventoryHelper(buildInventoryId(transferInventory.receivingStoreID(), transferInventory.tsin()))
                .thenCompose(inventory -> {
                    long totalQuantity = inventory.getTotalQuantity();
                    if (transferInventory.quantity() < 0) {
                        throw new IllegalArgumentException("Not enough units to receive: total=" + totalQuantity + ", quantity to receive=" + transferInventory.quantity());
                    }
                    inventory.setTotalQuantity(totalQuantity + transferInventory.quantity());
                    return addInventory(convertToInventoryInfo(inventory));
                });

        return sendingStoreInventory.thenCombine(receivingStoreInventory, InventoryTransferResponse::new);
    }

    private CompletableFuture<Inventory> findInventoryHelper(InventoryId inventoryId) {
        return inventoryRepository.findInventory(inventoryId);
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

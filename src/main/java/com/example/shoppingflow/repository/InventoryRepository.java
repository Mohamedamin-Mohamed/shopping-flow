package com.example.shoppingflow.repository;

import com.example.shoppingflow.DTO.InventoryId;
import com.example.shoppingflow.DTO.InventoryInfo;
import com.example.shoppingflow.model.Inventory;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.concurrent.CompletableFuture;

@Repository
public interface InventoryRepository extends JpaRepository<Inventory, InventoryId> {
    @Async
    CompletableFuture<Inventory> addInventory(InventoryInfo inventoryInfo);

    @Async
    CompletableFuture<Inventory> findInventory(InventoryId inventoryId);

    @Async
    CompletableFuture<List<Inventory>> findInventoryByTsin(String tsin);

    @Async
    CompletableFuture<List<Inventory>> findInventoryByStoreId(String storeId);


}

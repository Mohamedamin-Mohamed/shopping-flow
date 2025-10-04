package com.example.shoppingflow.repository;

import com.example.shoppingflow.DTO.InventoryId;
import com.example.shoppingflow.DTO.InventoryInfo;
import com.example.shoppingflow.model.Inventory;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.concurrent.CompletableFuture;

@Repository
public interface InventoryRepository extends JpaRepository<Inventory, InventoryId> {
    CompletableFuture<Inventory> addInventory(InventoryInfo inventoryInfo);
    CompletableFuture<Inventory> findInventory(InventoryId inventoryId);
    CompletableFuture<List<Inventory>> findInventoryByTsin(String tsin);
    CompletableFuture<List<Inventory>> findInventoryByStoreId(String storeId);

}

package com.example.shoppingflow.controller;

import com.example.shoppingflow.DTO.InventoryId;
import com.example.shoppingflow.DTO.InventoryInfo;
import com.example.shoppingflow.model.Inventory;
import com.example.shoppingflow.service.InventoryService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.concurrent.CompletableFuture;

@RestController
@RequestMapping("/inventory")
public class InventoryController {
    private final Logger logger = LoggerFactory.getLogger(InventoryController.class);
    private final InventoryService inventoryService;

    public InventoryController(InventoryService inventoryService) {
        this.inventoryService = inventoryService;
    }

    @PostMapping
    public ResponseEntity<CompletableFuture<Inventory>> addInventory(@RequestBody InventoryInfo inventoryInfo) {
        logger.info("Adding inventory for store {} of product {}", inventoryInfo.getStoreId(), inventoryInfo.getTsin());
        return ResponseEntity.status(HttpStatus.CREATED).body(inventoryService.addInventory(inventoryInfo));
    }

    @GetMapping
    public ResponseEntity<CompletableFuture<Inventory>> getInventory(@RequestBody InventoryId inventoryId) {
        logger.info("Getting inventory for store {} of product {}", inventoryId.getStoreId(), inventoryId.getTsin());
        return ResponseEntity.ok(inventoryService.findInventory(inventoryId));
    }

    @GetMapping
    public ResponseEntity<CompletableFuture<List<Inventory>>> getInventoryByTsin(@RequestParam String tsin) {
        logger.info("Getting inventory at all stores of product {}", tsin);
        return ResponseEntity.ok(inventoryService.findInventoryByTsin(tsin));
    }

    @GetMapping
    public ResponseEntity<CompletableFuture<List<Inventory>>> getInventoryByStoreId(@RequestParam String storeId) {
        logger.info("Getting all inventory at store {}", storeId);
        return ResponseEntity.ok(inventoryService.findInventoryByStoreId(storeId));
    }

    @PostMapping("/{tsin}/{storeId}/reserve")
    public ResponseEntity<CompletableFuture<Inventory>> reserveInventory(@PathVariable String storeId, String tsin,
                                                                         @RequestParam long quantity) {
        logger.info("Reserving inventory for store {} of product {} and quantity {}", tsin, storeId, quantity);
        return ResponseEntity.status(HttpStatus.CREATED).body(inventoryService.reserveInventory(storeId, tsin, quantity));
    }

    @PostMapping("/{tsin}/{storeId}/unreserve")
    public ResponseEntity<CompletableFuture<Inventory>> unreserveInventory(@PathVariable String storeId, String tsin,
                                                                           @RequestParam long quantity) {
        logger.info("Unreserving inventory for store {} of product {} and quantity {}", storeId, tsin, quantity);
        return ResponseEntity.status(HttpStatus.CREATED).body(inventoryService.unReserveInventory(storeId, tsin, quantity));
    }

}

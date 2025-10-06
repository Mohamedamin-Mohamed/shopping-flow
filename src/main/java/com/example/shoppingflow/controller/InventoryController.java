package com.example.shoppingflow.controller;

import com.example.shoppingflow.DTO.InventoryInfo;
import com.example.shoppingflow.DTO.InventoryTransferResponse;
import com.example.shoppingflow.DTO.TransferInventory;
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

    @GetMapping("/{storeId}/{tsin}")
    public ResponseEntity<CompletableFuture<Inventory>> getInventory(@PathVariable String storeId,
                                                                     @PathVariable String tsin) {
        logger.info("Getting inventory for store {} of product {}", storeId, tsin);
        return ResponseEntity.ok(inventoryService.findInventory(storeId, tsin));
    }

    @GetMapping("/product/{tsin}")
    public ResponseEntity<CompletableFuture<List<Inventory>>> getInventoryByTsin(@PathVariable String tsin) {
        logger.info("Getting inventory at all stores of product {}", tsin);
        return ResponseEntity.ok(inventoryService.findInventoryByTsin(tsin));
    }

    @GetMapping("/store/{storeId}")
    public ResponseEntity<CompletableFuture<List<Inventory>>> getInventoryByStoreId(@PathVariable String storeId) {
        logger.info("Getting all inventory at store {}", storeId);
        return ResponseEntity.ok(inventoryService.findInventoryByStoreId(storeId));
    }

    @PatchMapping("/{storeId}/{tsin}/reserve")
    public ResponseEntity<CompletableFuture<Inventory>> reserveInventory(@PathVariable String storeId,
                                                                         @PathVariable String tsin,
                                                                         @RequestParam long quantity) {
        logger.info("Reserving inventory for store {} of product {} and quantity {}", tsin, storeId, quantity);
        return ResponseEntity.ok(inventoryService.reserveInventory(storeId, tsin, quantity));
    }

    @PatchMapping("/{storeId}/{tsin}/unreserve")
    public ResponseEntity<CompletableFuture<Inventory>> unReserveInventory(@PathVariable String storeId,
                                                                           @PathVariable String tsin,
                                                                           @RequestParam long quantity) {
        logger.info("Un-reserving inventory for store {} of product {} and quantity {}", storeId, tsin, quantity);
        return ResponseEntity.ok(inventoryService.unReserveInventory(storeId, tsin, quantity));
    }

    @PostMapping("/{storeId}/{tsin}/purchase/confirm")
    public ResponseEntity<CompletableFuture<Inventory>> confirmPurchase(@PathVariable String storeId,
                                                                        @PathVariable String tsin,
                                                                        @RequestParam long quantity) {
        logger.info("Confirming purchase of {} units of product {} at store {}", quantity, tsin, storeId);
        return ResponseEntity.ok(inventoryService.confirmPurchase(storeId, tsin, quantity));
    }

    @PatchMapping("/{storeId}/{tsin}/restock")
    public ResponseEntity<CompletableFuture<Inventory>> restockItem(@PathVariable String storeId,
                                                                    @PathVariable String tsin,
                                                                    @RequestParam long quantity) {
        logger.info("Restocking {} units of product {} at store {}", quantity, tsin, storeId);
        return ResponseEntity.ok(inventoryService.restockItem(storeId, tsin, quantity));
    }

    @PatchMapping
    public ResponseEntity<CompletableFuture<InventoryTransferResponse>> transferInventory(@RequestBody TransferInventory transferInventory) {
        logger.info("Transferring inventory from store {} to store {} for product {}", transferInventory.sendingStoreId(), transferInventory.receivingStoreID(), transferInventory.tsin());
        return ResponseEntity.ok(inventoryService.transferInventory(transferInventory));
    }

}

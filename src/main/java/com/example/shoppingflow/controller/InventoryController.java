package com.example.shoppingflow.controller;

import com.example.shoppingflow.DTO.InventoryId;
import com.example.shoppingflow.DTO.InventoryInfo;
import com.example.shoppingflow.DTO.InventoryResponseInfo;
import com.example.shoppingflow.service.InventoryService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.concurrent.CompletableFuture;

@RestController
@RequestMapping("/inventory")
public class Inventory {
    private final Logger logger = LoggerFactory.getLogger(Inventory.class);
    private final InventoryService inventoryService;

    public Inventory(InventoryService inventoryService){
        this.inventoryService = inventoryService;
    }
    @PostMapping
    public ResponseEntity<CompletableFuture<InventoryResponseInfo>> addInventory(@RequestBody InventoryInfo inventoryInfo){
        logger.info("Adding inventory for store {} and product {}", inventoryInfo.getStoreId(), inventoryInfo.getTsin());
        return ResponseEntity.status(HttpStatus.CREATED).body(inventoryService.addInventory(inventoryInfo));
    }
}

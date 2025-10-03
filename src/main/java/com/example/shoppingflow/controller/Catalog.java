package com.example.shoppingflow.controller;

import com.example.shoppingflow.DTO.Product;
import com.example.shoppingflow.service.CatalogService;
import com.fasterxml.jackson.core.JsonProcessingException;
import jakarta.websocket.server.PathParam;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;

@RestController
@RequestMapping("/catalog")
public class Catalog {
    public Logger logger = LoggerFactory.getLogger(Catalog.class);
    private final CatalogService catalogService;

    public Catalog(CatalogService catalogService) {
        this.catalogService = catalogService;
    }

    @PostMapping
    public ResponseEntity<CompletableFuture<Void>> addProduct(@RequestBody Product product) throws JsonProcessingException {
        logger.info("Received request to add product with tsin {} to catalog", product.getTsin());
        return ResponseEntity.status(HttpStatus.CREATED).body(catalogService.addProduct(product));
    }

    @GetMapping("/{tsin}")
    public ResponseEntity<CompletableFuture<Product>> getProduct(@PathVariable String tsin) throws ExecutionException, InterruptedException, JsonProcessingException {
        logger.info("Getting product catalog of tsin {}", tsin);
        return ResponseEntity.ok(catalogService.getProduct(tsin));
    }

    @GetMapping
    public ResponseEntity<CompletableFuture<List<Product>>> queryProduct(@PathParam("tsin") String tsin, @PathParam("queryColumn") String queryColumn) {
        logger.info("Querying for product with partition key {} and column name {}", tsin, queryColumn);
        return ResponseEntity.ok(catalogService.queryProducts(tsin, queryColumn));
    }

    @PutMapping
    public ResponseEntity<CompletableFuture<Product>> updateProduct(@RequestBody Product product) throws ExecutionException, JsonProcessingException, InterruptedException {
        logger.info("Updating product of tsin {}", product.getTsin());
        return ResponseEntity.ok(catalogService.updateProduct(product));
    }

    @DeleteMapping("/{tsin}")
    public ResponseEntity<CompletableFuture<Product>> deleteProduct(@PathVariable String tsin) {
        logger.info("Deleting product with tsin {}", tsin);
        return ResponseEntity.ok(catalogService.deleteProduct(tsin));
    }

}

package com.example.shoppingflow.repository;

import com.example.shoppingflow.DTO.Product;
import com.example.shoppingflow.DTO.ProductPatchRequest;
import com.example.shoppingflow.model.Catalog;
import com.fasterxml.jackson.core.JsonProcessingException;
import software.amazon.awssdk.enhanced.dynamodb.model.PagePublisher;

import java.util.concurrent.CompletableFuture;

public interface DynamoQueryable {
    CompletableFuture<Void> addProduct(Product product) throws JsonProcessingException;

    CompletableFuture<Catalog> updateProduct(Product product) throws JsonProcessingException;

    CompletableFuture<Catalog> updateProductPartial(String tsin, ProductPatchRequest productPatchRequest);

    CompletableFuture<Catalog> getProduct(String tsin);

    PagePublisher<Catalog> queryProducts(String tsin, String queryColumn);

    CompletableFuture<Catalog> deleteProduct(String tsin);
}

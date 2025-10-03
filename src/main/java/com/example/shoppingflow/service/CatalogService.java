package com.example.shoppingflow.service;

import com.example.shoppingflow.DTO.Product;
import com.example.shoppingflow.model.Catalog;
import com.example.shoppingflow.repository.CatalogRepository;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.reactivestreams.Subscriber;
import org.reactivestreams.Subscription;
import org.springframework.stereotype.Service;
import software.amazon.awssdk.enhanced.dynamodb.model.PagePublisher;
import software.amazon.awssdk.services.dynamodb.model.AttributeValue;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;

@Service
public class CatalogService {
    private final CatalogRepository catalogRepository;
    private final ObjectMapper objectMapper = new ObjectMapper();
    private final com.example.shoppingflow.controller.Catalog catalog;

    public CatalogService(CatalogRepository catalogRepository, com.example.shoppingflow.controller.Catalog catalog) {
        this.catalogRepository = catalogRepository;
        this.catalog = catalog;
    }

    public CompletableFuture<Void> addProduct(Product product) throws JsonProcessingException {
        return catalogRepository.addProduct(product).thenApply(catalog -> catalog);
    }

    public CompletableFuture<Product> getProduct(String tsin) {
        CompletableFuture<Catalog> catalogCompletableFuture = catalogRepository.getProduct(tsin);
        return catalogCompletableFuture.thenApply(catalog -> {
            try {
                return convertToProduct(catalog);
            } catch (JsonProcessingException e) {
                throw new RuntimeException(e);
            }
        });
    }

    public CompletableFuture<Product> updateProduct(Product product) throws JsonProcessingException, ExecutionException, InterruptedException {
        CompletableFuture<Catalog> catalogCompletableFuture = catalogRepository.updateProduct(product);
        return catalogCompletableFuture.thenApply(catalog -> {
            try {
                return convertToProduct(catalog);
            } catch (JsonProcessingException e) {
                throw new RuntimeException(e);
            }
        });
    }

    public CompletableFuture<List<Product>> queryProducts(String tsin, String queryColumn) {
        PagePublisher<Catalog> catalogPagePublisher = catalogRepository.queryProducts(tsin, queryColumn);
        CompletableFuture<List<Product>> future = new CompletableFuture<>();
        List<Product> products = new ArrayList<>();

        catalogPagePublisher.items().subscribe(new Subscriber<>() {
            private Subscription subscription;

            @Override
            public void onSubscribe(Subscription subscription) {
                this.subscription = subscription;
                subscription.request(Long.MAX_VALUE);
            }

            @Override
            public void onNext(Catalog catalog) {
                try {
                    Product product = convertToProduct(catalog);
                    products.add(product);
                } catch (JsonProcessingException e) {
                    future.completeExceptionally(e);
                    subscription.cancel();
                }
            }

            @Override
            public void onError(Throwable throwable) {
                future.completeExceptionally(throwable);
                subscription.cancel();
            }

            @Override
            public void onComplete() {
                future.complete(products);
            }
        });

        return future;
    }

    public CompletableFuture<Product> deleteProduct(String tsin) {
        CompletableFuture<Catalog> catalogCompletableFuture = catalogRepository.deleteProduct(tsin);
        return catalogCompletableFuture.thenApply(catalog -> {
            try {
                return convertToProduct(catalog);
            } catch (JsonProcessingException e) {
                throw new RuntimeException(e);
            }
        });
    }

    private Product convertToProduct(Catalog catalog) throws JsonProcessingException {
        Product product = new Product();
        product.setTsin(catalog.getTsin());
        product.setName(catalog.getName());
        product.setPrice(catalog.getPrice());
        product.setAttributes(catalog.getAttributes());
        String imageInfo = convertToImageInfo(catalog);
        product.setImageInfo(imageInfo);
        return product;
    }

    private String convertToImageInfo(Catalog catalog) throws JsonProcessingException {
        AttributeValue attributeValue = catalog.getImageInfo();
        Map<String, AttributeValue> attributeValueMap = attributeValue.m();
        Map<String, Object> objectMap = new HashMap<>();

        for (Map.Entry<String, AttributeValue> entry : attributeValueMap.entrySet()) {
            String key = entry.getKey();
            AttributeValue value = entry.getValue();
            objectMap.put(key, value.s());
        }
        return objectMapper.writeValueAsString(objectMap);
    }
}

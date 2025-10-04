package com.example.shoppingflow.service;

import com.example.shoppingflow.DTO.Product;
import com.example.shoppingflow.DTO.ProductPatchRequest;
import com.example.shoppingflow.model.Catalog;
import com.example.shoppingflow.repository.CatalogRepository;
import com.fasterxml.jackson.core.JsonProcessingException;
import org.reactivestreams.Subscriber;
import org.reactivestreams.Subscription;
import org.springframework.stereotype.Service;
import software.amazon.awssdk.enhanced.dynamodb.model.PagePublisher;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CompletableFuture;

import static com.example.shoppingflow.utils.Util.convertToProduct;

@Service
public class CatalogService {
    private final CatalogRepository catalogRepository;

    public CatalogService(CatalogRepository catalogRepository) {
        this.catalogRepository = catalogRepository;
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

    public CompletableFuture<Product> updateProduct(Product product) throws JsonProcessingException {
        CompletableFuture<Catalog> catalogCompletableFuture = catalogRepository.updateProduct(product);
        return catalogCompletableFuture.thenApply(catalog -> {
            try {
                return convertToProduct(catalog);
            } catch (JsonProcessingException e) {
                throw new RuntimeException(e);
            }
        });
    }

    public CompletableFuture<Product> updateProductPartial(String tsin, ProductPatchRequest productPatchRequest) {
        CompletableFuture<Catalog> catalogCompletableFuture = catalogRepository.updateProductPartial(tsin, productPatchRequest);
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

}

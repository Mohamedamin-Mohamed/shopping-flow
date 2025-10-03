package com.example.shoppingflow.repository;

import com.example.shoppingflow.DTO.Product;
import com.example.shoppingflow.config.UtilConfig;
import com.example.shoppingflow.interfaces.DynamoQueryable;
import com.example.shoppingflow.model.Catalog;
import com.fasterxml.jackson.core.JsonProcessingException;
import org.springframework.stereotype.Repository;
import software.amazon.awssdk.enhanced.dynamodb.DynamoDbAsyncTable;
import software.amazon.awssdk.enhanced.dynamodb.DynamoDbEnhancedAsyncClient;
import software.amazon.awssdk.enhanced.dynamodb.Key;
import software.amazon.awssdk.enhanced.dynamodb.TableSchema;
import software.amazon.awssdk.enhanced.dynamodb.model.PagePublisher;
import software.amazon.awssdk.enhanced.dynamodb.model.QueryConditional;
import software.amazon.awssdk.enhanced.dynamodb.model.QueryEnhancedRequest;

import java.util.concurrent.CompletableFuture;

@Repository
public class CatalogRepository implements DynamoQueryable {
    private DynamoDbEnhancedAsyncClient dynamoClient;
    private UtilConfig utilConfig;


    public CatalogRepository(DynamoDbEnhancedAsyncClient dynamoDbEnhancedClient, UtilConfig utilConfig) {
        this.dynamoClient = dynamoDbEnhancedClient;
        this.utilConfig = utilConfig;
    }

    TableSchema<Catalog> tableSchema = TableSchema.fromClass(Catalog.class);
    DynamoDbAsyncTable<Catalog> dynamoDbAsyncTable = dynamoClient.table(utilConfig.getTableName(), tableSchema);

    @Override
    public CompletableFuture<Void> addProduct(Product product) throws JsonProcessingException {
        return dynamoDbAsyncTable.putItem(convertToCatalog(product));
    }

    @Override
    public CompletableFuture<Catalog> updateProduct(Product product) throws JsonProcessingException {
        return dynamoDbAsyncTable.updateItem(convertToCatalog(product));
    }

    @Override
    public CompletableFuture<Catalog> getProduct(String tsin) {
        return dynamoDbAsyncTable.getItem(partitionKeyBuilder(tsin));
    }

    @Override
    public PagePublisher<Catalog> queryProducts(String tsin, String queryColumnName) {
        return dynamoDbAsyncTable.query(enhancedRequest(tsin, queryColumnName));
    }

    @Override
    public CompletableFuture<Catalog> deleteProduct(String tsin) {
        return dynamoDbAsyncTable.deleteItem(partitionKeyBuilder(tsin));
    }

    private Key partitionKeyBuilder(String keyName) {
        return Key.builder().partitionValue(keyName).build();
    }

    private Key partitionSortKeyBuilder(String partitionKey, String sortKey){
        return Key.builder().partitionValue(partitionKey).sortValue(sortKey).build();
    }

    private QueryEnhancedRequest enhancedRequest(String hashKey, String rangeKey) {
        QueryConditional queryConditional = QueryConditional.keyEqualTo(partitionSortKeyBuilder(hashKey, rangeKey));

        return QueryEnhancedRequest.builder().queryConditional(queryConditional).build();
    }

    private Catalog convertToCatalog(Product product) throws JsonProcessingException {
        Catalog catalog = new Catalog();
        catalog.convertToCatalog(product);
        return catalog;
    }
}

package com.example.shoppingflow.utils;

import com.example.shoppingflow.DTO.Product;
import com.example.shoppingflow.model.Catalog;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import software.amazon.awssdk.services.dynamodb.model.AttributeValue;

import java.util.HashMap;
import java.util.Map;

public class Util {
    private static final ObjectMapper objectMapper = new ObjectMapper();

    public static Product convertToProduct(Catalog catalog) throws JsonProcessingException {
        Product product = new Product();
        product.setTsin(catalog.getTsin());
        product.setName(catalog.getName());
        product.setPrice(catalog.getPrice());
        product.setAttributes(catalog.getAttributes());
        String imageInfo = convertToImageInfo(catalog);
        product.setImageInfo(imageInfo);
        return product;
    }

    public static String convertToImageInfo(Catalog catalog) throws JsonProcessingException {
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

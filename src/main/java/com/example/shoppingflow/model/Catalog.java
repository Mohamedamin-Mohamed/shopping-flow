package com.example.shoppingflow.model;

import com.example.shoppingflow.DTO.ImageInfo;
import com.example.shoppingflow.DTO.Product;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import software.amazon.awssdk.core.SdkBytes;
import software.amazon.awssdk.enhanced.dynamodb.mapper.annotations.DynamoDbBean;
import software.amazon.awssdk.enhanced.dynamodb.mapper.annotations.DynamoDbIgnore;
import software.amazon.awssdk.enhanced.dynamodb.mapper.annotations.DynamoDbPartitionKey;
import software.amazon.awssdk.enhanced.dynamodb.mapper.annotations.DynamoDbSortKey;
import software.amazon.awssdk.services.dynamodb.model.AttributeValue;

import java.util.HashMap;
import java.util.Map;

@DynamoDbBean
public class Catalog {
    private final Logger logger = LoggerFactory.getLogger(Catalog.class);

    private String tsin;

    @DynamoDbPartitionKey
    public String getTsin() {
        return tsin;
    }

    private String name;

    @DynamoDbSortKey
    public String getName() {
        return name;
    }

    private String category;
    private String price;
    private String attributes;
    private AttributeValue imageInfo;
    private long inventoryCount;

    public String getCategory() {
        return category;
    }

    public String getPrice() {
        return price;
    }

    public String getAttributes() {
        return attributes;
    }

    public AttributeValue getImageInfo() {
        return imageInfo;
    }

    public long getInventoryCount() {
        return inventoryCount;
    }

    @DynamoDbIgnore
    public void logCatalogInfo() {
        logger.info("TSIN {}, name {}, category {}, price {}, attributes{}, image name {}, inventory count {}",
                tsin, name, category, price, attributes, getImageName(), inventoryCount);
    }

    @DynamoDbIgnore
    private String getImageName() {
        Map<String, AttributeValue> map = imageInfo.m();
        AttributeValue imageAttribute = map.get("imageName");
        String imageName = imageAttribute.s();
        logger.info("Image name is {}", imageName);
        return imageName;
    }

    @DynamoDbIgnore
    public void convertToCatalog(Product product) throws JsonProcessingException {
        Catalog catalog = new Catalog();
        catalog.tsin = product.getTsin();
        catalog.name = product.getName();
        catalog.category = product.getCategory();
        catalog.price = product.getPrice();
        catalog.attributes = product.getAttributes();
        catalog.imageInfo = convertToImageInfo(product.getImageInfo());
        catalog.inventoryCount = product.getInventoryCount();
    }

    @DynamoDbPartitionKey
    private AttributeValue convertToImageInfo(String imageInfo) throws JsonProcessingException {
        ObjectMapper objectMapper = new ObjectMapper();
        ImageInfo info = objectMapper.readValue(imageInfo, ImageInfo.class);

        Map<String, AttributeValue> attributeValueMap = new HashMap<>();
        attributeValueMap.put("imageName", AttributeValue.fromS(info.imageName()));
        attributeValueMap.put("image", AttributeValue.fromB(SdkBytes.fromByteArray(info.image())));

        return AttributeValue.builder().m(attributeValueMap).build();
    }
}

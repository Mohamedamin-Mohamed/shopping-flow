package com.example.shoppingflow.DTO;

public class Product {
    private String tsin;
    private String name;
    private String category;
    private String price;
    private String attributes;
    private String imageInfo;
    private long inventoryCount;

    public void setTsin(String tsin) {
        this.tsin = tsin;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public void setAttributes(String attributes) {
        this.attributes = attributes;
    }

    public void setImageInfo(String imageInfo) {
        this.imageInfo = imageInfo;
    }

    public void setInventoryCount(long inventoryCount) {
        this.inventoryCount = inventoryCount;
    }

    public String getTsin() {
        return tsin;
    }

    public String getName() {
        return name;
    }

    public String getCategory() {
        return category;
    }

    public String getPrice() {
        return price;
    }

    public String getAttributes() {
        return attributes;
    }

    public String imageInfo() {
        return imageInfo;
    }

    public String getImageInfo() {
        return imageInfo;
    }

    public long getInventoryCount(){
        return inventoryCount;
    }

}

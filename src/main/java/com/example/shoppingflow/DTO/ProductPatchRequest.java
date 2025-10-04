package com.example.shoppingflow.DTO;

public record ProductPatchRequest(String tsin, String name, String category, String price, String attributes,
                                  String imageInfo, long inventoryCount) {
}

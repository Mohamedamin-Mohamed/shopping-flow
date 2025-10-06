package com.example.shoppingflow.DTO;


import java.math.BigDecimal;

public record Item(String tsin, String storeId, String addedAt, int quantity, BigDecimal price) {
}

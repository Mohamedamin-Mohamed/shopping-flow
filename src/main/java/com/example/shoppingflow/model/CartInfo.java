package com.example.shoppingflow.model;

import com.example.shoppingflow.DTO.Item;

public record CartInfo(String cartId, Item[] items, String createdAt, String expiresAt) {
}

package com.example.shoppingflow.DTO;

import java.util.List;

public class OrderRequest {
    private List<Item> items;
    private Address address;
    private String idempotencyKey;
    private String paymentMethod;
    private String currency;
    private String pickupMode;

    public List<Item> getItems() {
        return items;
    }

    public Address getAddress() {
        return address;
    }

    public String getIdempotencyKey(){
        return idempotencyKey;
    }

    public String getPaymentMethod() {
        return paymentMethod;
    }

    public String getCurrency() {
        return currency;
    }

    public String getPickupMode(){
        return pickupMode;
    }

}

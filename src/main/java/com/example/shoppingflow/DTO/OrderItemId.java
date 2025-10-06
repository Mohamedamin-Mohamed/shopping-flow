package com.example.shoppingflow.DTO;

import jakarta.persistence.Embeddable;

import java.io.Serializable;
import java.util.Objects;

@Embeddable
public class OrderItemId implements Serializable {
    private String orderId;
    private String tsin;

    public String getOrderId() {
        return orderId;
    }

    public void setOrderId(String orderId) {
        this.orderId = orderId;
    }

    public String getTsin() {
        return tsin;
    }

    public void setTsin(String tsin) {
        this.tsin = tsin;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;

        OrderItemId other = (OrderItemId) obj;
        return other.orderId.equals(orderId) && other.tsin.equals(tsin);
    }

    @Override
    public int hashCode() {
        return Objects.hash(orderId, tsin);
    }
}

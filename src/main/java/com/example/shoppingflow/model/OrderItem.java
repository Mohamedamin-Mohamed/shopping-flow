package com.example.shoppingflow.model;

import com.example.shoppingflow.DTO.OrderItemId;
import jakarta.persistence.*;

import java.math.BigDecimal;

@Entity(name = "order_item")
public class OrderItem {
    @EmbeddedId
    private OrderItemId orderItemId;

    @ManyToOne
    @MapsId("orderId") //uses the already defined column in the composite primary key called orderId
    @JoinColumn(name = "orderId") //just to confirm to use orderId as the name of the foreign key column
    private Order order;

    public String getStoreId() {
        return storeId;
    }

    public void setStoreId(String storeId) {
        this.storeId = storeId;
    }

    private String storeId;
    private int quantity;
    private BigDecimal price;

    public OrderItemId getOrderItemId() {
        return orderItemId;
    }

    public void setOrderItemId(OrderItemId orderItemId) {
        this.orderItemId = orderItemId;
    }

    public Order getOrder() {
        return order;
    }

    public void setOrder(Order order) {
        this.order = order;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public BigDecimal getPrice() {
        return price;
    }

    public void setPrice(BigDecimal price) {
        this.price = price;
    }
}

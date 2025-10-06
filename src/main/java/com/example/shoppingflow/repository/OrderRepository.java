package com.example.shoppingflow.repository;

import com.example.shoppingflow.model.Order;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;
import java.util.concurrent.CompletableFuture;

@Repository
public interface OrderRepository extends JpaRepository<Order, Long> {
    CompletableFuture<Order> saveOrder(Order order);
    CompletableFuture<Order> findByOrderId(UUID orderId);
}

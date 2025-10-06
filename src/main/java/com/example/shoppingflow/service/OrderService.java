package com.example.shoppingflow.service;

import com.example.shoppingflow.DTO.OrderItemId;
import com.example.shoppingflow.DTO.OrderRequest;
import com.example.shoppingflow.DTO.OrderStatus;
import com.example.shoppingflow.DTO.PaymentStatus;
import com.example.shoppingflow.model.Inventory;
import com.example.shoppingflow.model.Order;
import com.example.shoppingflow.model.OrderItem;
import com.example.shoppingflow.repository.OrderRepository;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.asynchttpclient.AsyncHttpClient;
import org.asynchttpclient.Response;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import software.amazon.awssdk.http.Header;

import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;

@Service
public class OrderService {
    private final OrderRepository orderRepository;
    private final AsyncHttpClient asyncHttpClient;
    private final ObjectMapper objectMapper;

    public OrderService(OrderRepository orderRepository, AsyncHttpClient asyncHttpClient) {
        this.orderRepository = orderRepository;
        this.asyncHttpClient = asyncHttpClient;
        objectMapper = new ObjectMapper();
    }

    public CompletableFuture<Order> createOrder(OrderRequest orderRequest) {
        return orderRepository.saveOrder(buildOrder(orderRequest)).thenApply(order -> {
            //here loop through the order items and reserve inventory for each item
            inventoryInteractionHelper(order, "reserve");
            return order;
        });
    }

    public CompletableFuture<Order> initiatePayment(UUID orderId) {
        //calls payment service/payment provider and provide webhook url so that payment is confirmed
        return orderRepository.findByOrderId(orderId).thenApply(order -> {
            //change order PAYMENT_STATUS
            order.setPaymentStatus(PaymentStatus.INITIATED);
            return order;
        });
    }

    public CompletableFuture<Order> confirmPayment(UUID orderId) {
        //here we actually perform the actual decrement of inventory for each item
        return orderRepository.findByOrderId(orderId).thenApply(order -> {
            inventoryInteractionHelper(order, "purchase/confirm");
            order.setPaymentStatus(PaymentStatus.CONFIRMED);
            return order;
        });
    }

    private void inventoryInteractionHelper(Order order, String path) {
        order.getItems().forEach(orderItem -> {
            String storeId = orderItem.getStoreId();
            String tsin = orderItem.getOrderItemId().getTsin();

            CompletableFuture<Response> inventoryCompletableFuture = asyncHttpClient
                    .preparePost(String.format("/inventory/%s/%s/%s", storeId, tsin, path))
                    .setHeader(Header.CONTENT_TYPE, MediaType.APPLICATION_JSON)
                    .addQueryParam("quantity", String.valueOf(orderItem.getQuantity()))
                    .execute().toCompletableFuture();

            inventoryCompletableFuture.thenAccept(response -> {
                String body = response.getResponseBody();
                try {
                    Inventory inventory = objectMapper.readValue(body, Inventory.class);

                } catch (JsonProcessingException e) {
                    throw new RuntimeException(e);
                }
            });
        });
    }


    private Order buildOrder(OrderRequest orderRequest) {
        Order order = new Order();
        order.setOrderId(UUID.randomUUID());
        List<OrderItem> orderItems = orderRequest.getItems().stream().map(item -> {
            OrderItem orderItem = new OrderItem();
            OrderItemId orderItemId = new OrderItemId();

            orderItemId.setOrderId(order.getOrderId().toString());
            orderItemId.setTsin(item.tsin());

            orderItem.setOrderItemId(orderItemId);
            orderItem.setOrder(order);
            orderItem.setStoreId(item.storeId());
            orderItem.setPrice(item.price());
            orderItem.setQuantity(item.quantity());

            return orderItem;
        }).toList();

        order.setItems(orderItems);
        order.setShippingAddress(orderRequest.getAddress());

        //for now set it to new, we'll change it when payment is initiated to PENDING_PAYMENT
        order.setOrderStatus(OrderStatus.NEW);
        BigDecimal totalAmount = getTotalAmount(orderItems);
        order.setTotalAmount(totalAmount);
        order.setCurrency(orderRequest.getCurrency());
        order.setPaymentStatus(PaymentStatus.PENDING);

        return order;
    }

    private BigDecimal getTotalAmount(List<OrderItem> orderItems) {
        return orderItems.stream()
                .map(orderItem -> orderItem.getPrice().multiply(new BigDecimal(orderItem.getQuantity())))
                .reduce(BigDecimal.ZERO, BigDecimal::add);
    }
}

package com.example.shoppingflow.controller;

import com.example.shoppingflow.DTO.OrderRequest;
import com.example.shoppingflow.Exception.ClientException;
import com.example.shoppingflow.model.Order;
import com.example.shoppingflow.service.OrderService;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;
import java.util.concurrent.CompletableFuture;

@RestController
@RequestMapping("/orders")
public class OrderController {
    private final Logger logger = LoggerFactory.getLogger(OrderController.class);
    private final ObjectMapper objectMapper = new ObjectMapper();
    private final OrderService orderService;


    public OrderController(OrderService orderService) {
        this.orderService = orderService;
    }

    @PostMapping
    public ResponseEntity<CompletableFuture<Order>> createOrder(@RequestBody OrderRequest orderRequest) throws JsonProcessingException {
        try {
            logger.info("Creating order: {}", objectMapper.writeValueAsString(orderRequest));
            return ResponseEntity.status(HttpStatus.CREATED).body(orderService.createOrder(orderRequest));
        } catch (JsonProcessingException e) {
            throw new ClientException("Order creation failed, order request= " + objectMapper.writeValueAsString(orderRequest));
        }
    }

    @PostMapping("/{orderId}/payment/initiate")
    public ResponseEntity<CompletableFuture<Order>> initiatePayment(@PathVariable UUID orderId) {
        logger.info("Initiating payment for order id{}", orderId);
        return ResponseEntity.status(HttpStatus.CREATED).body(orderService.initiatePayment(orderId));
    }

    //webhook to be used by either payment provider or the payment service to confirm payment went through
    @PatchMapping("/{orderId}/payment/confirm")
    public ResponseEntity<CompletableFuture<Order>> confirmPayment(@PathVariable UUID orderId) {
        logger.info("Confirming payment for order id {}", orderId);
        return ResponseEntity.ok(orderService.confirmPayment(orderId));
    }

    @PatchMapping("/{orderId}/payment/fail")
    public ResponseEntity<CompletableFuture<Order>> paymentFail(@PathVariable UUID orderId) {
        logger.info("Request for payment failure of order id {}", orderId);
        return ResponseEntity.ok(orderService.paymentFail(orderId));
    }

    @PatchMapping("/{orderId}/payment/retry")
    public ResponseEntity<CompletableFuture<Order>> paymentRetry(@PathVariable UUID orderId){

    }
}

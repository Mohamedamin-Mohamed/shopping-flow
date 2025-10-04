package com.example.shoppingflow.controller;

import com.example.shoppingflow.DTO.CartCreationInfo;
import com.example.shoppingflow.DTO.CartResponseInfo;
import com.example.shoppingflow.model.CartInfo;
import com.example.shoppingflow.service.CartService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/cart")
public class Cart {
    private final CartService cartService;
    private final Logger logger = LoggerFactory.getLogger(Cart.class);

    public Cart(CartService cartService) {
        this.cartService = cartService;
    }

    @PostMapping
    public ResponseEntity<CartResponseInfo> createCart(@RequestBody CartCreationInfo cartCreationInfo) {
        logger.info("Creating cart session");
        return ResponseEntity.ok(cartService.createCart(cartCreationInfo));
    }

    @PostMapping("/{cartId}/items")
    public ResponseEntity<CartResponseInfo> addItem(@RequestBody CartInfo cartInfo) {
        logger.info("Adding item to cart of id {}", cartInfo.cartId());
        return ResponseEntity.ok(cartService.addItem(cartInfo));
    }

    @GetMapping("/{cartId}")
    public ResponseEntity<List<CartInfo>> getCartInfo(@PathVariable String cartId) {
        logger.info("Getting cart info for cart id {}", cartId);
        return ResponseEntity.ok(cartService.getCartInfo(cartId));
    }

}

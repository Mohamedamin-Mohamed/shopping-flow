package com.example.shoppingflow.service;

import com.example.shoppingflow.DTO.CartCreationInfo;
import com.example.shoppingflow.DTO.CartResponseInfo;
import com.example.shoppingflow.config.RedisConfig;
import com.example.shoppingflow.model.CartInfo;
import com.example.shoppingflow.repository.CartRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CartService {
    private final RedisConfig redisConfig;
    private final CartRepository cartRepository;

    public CartService(RedisConfig redisConfig, CartRepository cartRepository) {
        this.redisConfig = redisConfig;
        this.cartRepository = cartRepository;
    }

    public CartResponseInfo createCart(CartCreationInfo cartCreationInfo) {
        return null;
    }

    public CartResponseInfo addItem(CartInfo cartInfo) {
        return cartRepository.saveItem(cartInfo);
    }

    public List<CartInfo> getCartInfo(String cartId) {
        return cartRepository.findCartInfo(cartId);
    }
}

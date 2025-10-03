package com.example.shoppingflow.repository;

import com.example.shoppingflow.DTO.CartResponseInfo;
import com.example.shoppingflow.model.CartInfo;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;

@Repository
public class CartRepository {

    public CartResponseInfo saveItem(CartInfo cartInfo) {
        return null;
    }

    public List<CartInfo> findCartInfo(String cartId) {
        return new ArrayList<>();
    }

}

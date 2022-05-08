package com.es.phoneshop.dao;

import com.es.phoneshop.model.cart.CartItem;

import java.util.List;

public interface OrderItemDao {
    void save(List<CartItem> cartItems);
}

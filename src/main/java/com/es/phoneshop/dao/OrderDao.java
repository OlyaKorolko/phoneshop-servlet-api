package com.es.phoneshop.dao;

import com.es.phoneshop.model.cart.CartItem;
import com.es.phoneshop.model.order.Order;

import java.util.List;
import java.util.Optional;

public interface OrderDao {
    Optional<Order> findById(Long id);

    Optional<Order> findBySecureId(String id);

    void save(Order order, List<CartItem> cartItems);

    List<Order> findAll(String sessionId);
}

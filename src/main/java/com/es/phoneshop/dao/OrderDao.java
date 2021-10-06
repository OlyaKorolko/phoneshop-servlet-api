package com.es.phoneshop.dao;

import com.es.phoneshop.model.order.Order;

import java.util.List;

public interface OrderDao {
    Order getItem(Long id);

    Order getOrderBySecureId(String id);

    void save(Order order);

    List<Order> findOrders();
}

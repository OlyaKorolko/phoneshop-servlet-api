package com.es.phoneshop.dao;

import com.es.phoneshop.model.cart.CartItem;
import com.es.phoneshop.model.product.Product;

import java.util.List;
import java.util.Optional;

public interface ProductDao {
    Optional<Product> findById(Long id);

    List<Product> findAll();

    void save(Product product);

    List<CartItem> findByOrderId(Long orderId);
}

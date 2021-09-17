package com.es.phoneshop.dao;

import com.es.phoneshop.model.Product;

import java.util.List;
import java.util.Optional;

public interface ProductDao {
    Product getProduct(Long id);
    List<Product> findProducts(String query, String sortField, String sortOrder);
    void save(Product product);
    void delete(Long id);
}

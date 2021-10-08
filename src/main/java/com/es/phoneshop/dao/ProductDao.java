package com.es.phoneshop.dao;

import com.es.phoneshop.enums.AdvancedSortParam;
import com.es.phoneshop.enums.Selector;
import com.es.phoneshop.model.product.Product;

import java.math.BigDecimal;
import java.util.List;

public interface ProductDao {
    Product getItem(Long id);

    List<Product> findProducts(String query, String sortField, String sortOrder);

    List<Product> findProducts(String query, Selector wordSelector, BigDecimal minPrice, BigDecimal maxPrice);

    void save(Product product);

    void delete(Long id);
}

package com.es.phoneshop.dao;

import com.es.phoneshop.dto.ProductListPageFilterDto;
import com.es.phoneshop.model.product.Product;

import java.util.List;
import java.util.Optional;

public interface ProductDao {
    Product getItem(Long id);

    List<Product> findProducts(ProductListPageFilterDto productListPageFilter);

    void save(Product product);

    void delete(Long id);
}

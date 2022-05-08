package com.es.phoneshop.service;

import com.es.phoneshop.dto.ProductListPageFilterDto;
import com.es.phoneshop.model.product.Product;

import java.util.List;

public interface ProductService {
    List<Product> findByFilter(ProductListPageFilterDto productListPageFilter);

    Product findById(Long id);
}

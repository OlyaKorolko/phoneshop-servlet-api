package com.es.phoneshop.service.impl;

import com.es.phoneshop.dao.ProductDao;
import com.es.phoneshop.dto.ProductListPageFilterDto;
import com.es.phoneshop.enums.SortField;
import com.es.phoneshop.enums.SortOrder;
import com.es.phoneshop.exception.ProductNotFoundException;
import com.es.phoneshop.model.product.Product;
import com.es.phoneshop.service.ProductService;
import lombok.AllArgsConstructor;

import java.util.Arrays;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

@AllArgsConstructor
public class DefaultProductService implements ProductService {
    private final ProductDao productDao;

    @Override
    public List<Product> findByFilter(ProductListPageFilterDto productListPageFilter) {
        List<Product> products = productDao.findAll();
        if (productListPageFilter.getQuery().isPresent()) {
            products = sortByQuery(productListPageFilter.getQuery().get(), products);
        }
        if (productListPageFilter.getSortField().isPresent() && productListPageFilter.getSortOrder().isPresent()) {
            products = sortByField(products, productListPageFilter.getSortField().get(),
                    productListPageFilter.getSortOrder().get());
        }
        return products;
    }

    @Override
    public Product findById(Long id) {
        if (productDao.findById(id).isPresent()) {
            return productDao.findById(id).get();
        } else throw new ProductNotFoundException("Product with id " + id + " was not found");
    }

    private double compareByRelevancy(Product product, List<String> tokens) {
        long tokenNumber = 0;
        if (product.getDescription() != null && !product.getDescription().equals("")) {
            tokenNumber = tokens.stream().filter(token -> product.getDescription().contains(token)).count();
            return tokenNumber / (double) product.getDescription().split("\\s").length;
        }
        return tokenNumber;
    }

    private List<Product> sortByQuery(String query, List<Product> products) {
        List<String> tokens = Arrays.asList(query.split("\\s"));
        Comparator<Product> comparator =
                Comparator.comparingDouble(product -> compareByRelevancy(product, tokens));
        return products.stream()
                .filter(product -> tokens.stream()
                        .anyMatch(token -> product.getDescription() != null && product.getDescription().contains(token)))
                .sorted(comparator.reversed())
                .collect(Collectors.toList());
    }

    private Comparator<Product> getComparatorBySortField(String sortField, String sortOrder) {
        Comparator<Product> comparator = null;
        if (SortField.DESCRIPTION.toString().equalsIgnoreCase(sortField)) {
            comparator = Comparator.comparing(Product::getDescription);
        } else if (SortField.PRICE.toString().equalsIgnoreCase(sortField)) {
            comparator = Comparator.comparing(Product::getPrice);
        }
        if (SortOrder.DESC.toString().equalsIgnoreCase(sortOrder) && comparator != null) {
            return comparator.reversed();
        }
        return comparator;
    }

    private List<Product> sortByField(List<Product> products, String sortField, String sortOrder) {
        Comparator<Product> comp = getComparatorBySortField(sortField, sortOrder);
        if (comp != null) {
            return products.stream()
                    .sorted(comp)
                    .collect(Collectors.toList());
        } else {
            return products;
        }
    }
}

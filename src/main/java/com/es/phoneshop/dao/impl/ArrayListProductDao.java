package com.es.phoneshop.dao.impl;

import com.es.phoneshop.dao.ProductDao;
import com.es.phoneshop.exception.ProductNotFoundException;
import com.es.phoneshop.enums.SortField;
import com.es.phoneshop.enums.SortOrder;
import com.es.phoneshop.model.Product;

import java.util.*;
import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;
import java.util.stream.Collectors;

public class ArrayListProductDao implements ProductDao {
    private long maxId;
    private final List<Product> products;
    private final ReadWriteLock readWriteLock = new ReentrantReadWriteLock();
    private static volatile ProductDao instance;

    public static synchronized ProductDao getInstance() {
        if (instance == null) {
            synchronized (ArrayListProductDao.class) {
                if (instance == null) {
                    instance = new ArrayListProductDao();
                }
            }
        }
        return instance;
    }

    private ArrayListProductDao() {
        maxId = 0;
        products = new ArrayList<>();
    }

    @Override
    public Product getProduct(Long id) {
        readWriteLock.readLock().lock();
        try {
            return products.stream()
                    .filter(this::checkProductInStock)
                    .filter(pr -> pr.getId().equals(id))
                    .findAny()
                    .orElseThrow(() -> new ProductNotFoundException("Product with id: " + id + " was not found"));
        } finally {
            readWriteLock.readLock().unlock();
        }
    }

    private boolean checkProductInStock(Product product) {
        return product.getPrice() != null && product.getStock() > 0;
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

    @Override
    public List<Product> findProducts(String query, String sortField, String sortOrder) {
        readWriteLock.readLock().lock();
        List<Product> fetchedProducts;
        try {
            fetchedProducts = products.stream()
                    .filter(this::checkProductInStock)
                    .collect(Collectors.toList());
            if (query != null && !query.isEmpty()) {
                fetchedProducts = sortByQuery(query, fetchedProducts);
            }
            if (sortField != null && !sortField.isEmpty() && sortOrder != null && !sortOrder.isEmpty()) {
                fetchedProducts = sortByField(fetchedProducts, sortField, sortOrder);
            }
            return fetchedProducts;
        } finally {
            readWriteLock.readLock().unlock();
        }
    }

    @Override
    public void save(Product product) {
        readWriteLock.writeLock().lock();
        try {
            Optional<Product> oldProduct = products.stream().filter(pr -> pr.getId().equals(product.getId())).findAny();
            if (oldProduct.isPresent()) {
                int index = products.indexOf(oldProduct.get());
                products.set(index, product);
            } else {
                product.setId(maxId++);
                products.add(product);
            }
        } finally {
            readWriteLock.writeLock().unlock();
        }
    }

    @Override
    public void delete(Long id) {
        readWriteLock.writeLock().lock();
        try {
            products.removeIf(product -> product.getId().equals(id));
        } finally {
            readWriteLock.writeLock().unlock();
        }
    }
}

package com.es.phoneshop.dao.impl.in_memory;

import com.es.phoneshop.dao.ProductDao;
import com.es.phoneshop.model.cart.CartItem;
import com.es.phoneshop.model.product.Product;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class ArrayListProductDao implements ProductDao {
    private final List<Product> products = new ArrayList<>();
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

    private boolean checkProductInStock(Product product) {
        return product.getPrice() != null && product.getStock() > 0;
    }

    @Override
    public Optional<Product> findById(Long id) {
        readWriteLock.readLock().lock();
        try {
            return products.stream()
                    .filter((o -> o.getId().equals(id)))
                    .findFirst();
        } finally {
            readWriteLock.readLock().unlock();
        }
    }

    @Override
    public List<Product> findAll() {
        readWriteLock.readLock().lock();
        try {
            return products.stream()
                    .filter(this::checkProductInStock).toList();
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
                product.setId((long) products.size());
                products.add(product);
            }
        } finally {
            readWriteLock.writeLock().unlock();
        }
    }

    @Override
    public List<CartItem> findByOrderId(Long orderId) {
        throw new UnsupportedOperationException();
    }
}

package com.es.phoneshop.dao.impl.in_memory;

import com.es.phoneshop.dao.OrderDao;
import com.es.phoneshop.model.cart.CartItem;
import com.es.phoneshop.model.order.Order;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class ArrayListOrderDao implements OrderDao {
    private final List<Order> orders = new ArrayList<>();
    private final ReadWriteLock readWriteLock = new ReentrantReadWriteLock();
    private static volatile OrderDao instance;

    public static synchronized OrderDao getInstance() {
        if (instance == null) {
            synchronized (ArrayListOrderDao.class) {
                if (instance == null) {
                    instance = new ArrayListOrderDao();
                }
            }
        }
        return instance;
    }

    @Override
    public Optional<Order> findById(Long id) {
        readWriteLock.readLock().lock();
        try {
            return orders.stream()
                    .filter((o -> o.getId().equals(id))).findFirst();
        } finally {
            readWriteLock.readLock().unlock();
        }
    }

    @Override
    public Optional<Order> findBySecureId(String id) {
        readWriteLock.readLock().lock();
        try {
            return orders.stream()
                    .filter(o -> o.getSecureId().equals(id)).findFirst();
        } finally {
            readWriteLock.readLock().unlock();
        }
    }

    @Override
    public void save(Order order, List<CartItem> cartItems) {
        readWriteLock.writeLock().lock();
        try {
            if (order != null) {
                order.setId((long) orders.size());
                orders.add(order);
            }
        } finally {
            readWriteLock.writeLock().unlock();
        }
    }

    @Override
    public List<Order> findAll(String sessionId) {
        return orders;
    }
}

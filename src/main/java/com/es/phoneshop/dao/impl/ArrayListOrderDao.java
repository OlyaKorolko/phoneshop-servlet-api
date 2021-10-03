package com.es.phoneshop.dao.impl;

import com.es.phoneshop.dao.OrderDao;
import com.es.phoneshop.exception.OrderNotFoundException;
import com.es.phoneshop.model.order.Order;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;

public class ArrayListOrderDao implements OrderDao {
    private long maxId;
    private final List<Order> orders;
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

    private ArrayListOrderDao() {
        orders = new ArrayList<>();
    }

    @Override
    public Order getOrder(Long id) {
        readWriteLock.readLock().lock();
        try {
            return orders.stream()
                    .filter(o -> o.getId().equals(id))
                    .findAny()
                    .orElseThrow(() -> new OrderNotFoundException("Order with id: " + id + " was not found"));
        } finally {
            readWriteLock.readLock().unlock();
        }
    }

    @Override
    public Order getOrderBySecureId(String id) {
        readWriteLock.readLock().lock();
        try {
            return orders.stream()
                    .filter(o -> o.getSecureId().equals(id))
                    .findAny()
                    .orElseThrow(() -> new OrderNotFoundException("Your order was not found"));
        } finally {
            readWriteLock.readLock().unlock();
        }
    }

    @Override
    public void save(Order order) {
        readWriteLock.writeLock().lock();
        try {
            order.setId(maxId++);
            orders.add(order);
        } finally {
            readWriteLock.writeLock().unlock();
        }
    }

    @Override
    public List<Order> findOrders() {
        return orders;
    }
}

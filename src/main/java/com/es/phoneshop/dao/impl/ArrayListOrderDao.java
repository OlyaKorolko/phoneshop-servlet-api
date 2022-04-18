package com.es.phoneshop.dao.impl;

import com.es.phoneshop.dao.AbstractDao;
import com.es.phoneshop.dao.OrderDao;
import com.es.phoneshop.exception.OrderNotFoundException;
import com.es.phoneshop.model.order.Order;

import java.util.List;
import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;

public class ArrayListOrderDao extends AbstractDao<Order> implements OrderDao {
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
        orders = super.getObjects();
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
    public List<Order> findOrders() {
        return orders;
    }
}

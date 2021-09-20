package com.es.phoneshop.service.impl;

import com.es.phoneshop.model.product.Product;
import com.es.phoneshop.model.viewHistory.ViewHistory;
import com.es.phoneshop.service.ViewHistoryService;

import javax.servlet.http.HttpServletRequest;
import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;

public class DefaultViewHistoryService implements ViewHistoryService {
    private static volatile ViewHistoryService instance;
    private static final String VIEW_HISTORY_SESSION_ATTRIBUTE = DefaultViewHistoryService.class.getName() + ".viewHistory";
    private final ReadWriteLock readWriteLock = new ReentrantReadWriteLock();

    public static synchronized ViewHistoryService getInstance() {
        if (instance == null) {
            synchronized (DefaultCartService.class) {
                if (instance == null) {
                    instance = new DefaultViewHistoryService() {
                    };
                }
            }
        }
        return instance;
    }
    @Override
    public void addToHistory(ViewHistory viewHistory, Product product) {
        readWriteLock.writeLock().lock();
        try {
            viewHistory.addToHistory(product);
        } finally {
            readWriteLock.writeLock().unlock();
        }
    }

    @Override
    public ViewHistory getViewHistory(HttpServletRequest request) {
        readWriteLock.readLock().lock();
        try {
            ViewHistory viewHistory = (ViewHistory) request.getSession().getAttribute(VIEW_HISTORY_SESSION_ATTRIBUTE);
            if (viewHistory == null) {
                request.getSession().setAttribute(VIEW_HISTORY_SESSION_ATTRIBUTE, viewHistory = new ViewHistory());
            }
            return viewHistory;
        } finally {
            readWriteLock.readLock().unlock();
        }
    }
}

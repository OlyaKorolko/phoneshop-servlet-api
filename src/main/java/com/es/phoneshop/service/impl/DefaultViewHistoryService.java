package com.es.phoneshop.service.impl;

import com.es.phoneshop.model.product.Product;
import com.es.phoneshop.model.viewHistory.ViewHistory;
import com.es.phoneshop.service.ViewHistoryService;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

import javax.servlet.http.HttpServletRequest;
import java.util.List;
import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class DefaultViewHistoryService implements ViewHistoryService {
    private static volatile ViewHistoryService instance;
    private static final String VIEW_HISTORY_SESSION_ATTRIBUTE = "viewHistory";
    private final ReadWriteLock readWriteLock = new ReentrantReadWriteLock();

    public static synchronized ViewHistoryService getInstance() {
        if (instance == null) {
            synchronized (DefaultCartService.class) {
                if (instance == null) {
                    instance = new DefaultViewHistoryService();
                }
            }
        }
        return instance;
    }

    @Override
    public void addToHistory(List<Product> viewHistory, Product product) {
        readWriteLock.writeLock().lock();
        try {
            if (!viewHistory.contains(product)) {
                if (viewHistory.size() == ViewHistory.CAPACITY) {
                    viewHistory.remove(0);
                }
                viewHistory.add(product);
            } else if (viewHistory.size() > 1) {
                viewHistory.remove(product);
                viewHistory.add(product);
            }
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

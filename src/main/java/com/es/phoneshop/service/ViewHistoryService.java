package com.es.phoneshop.service;

import com.es.phoneshop.model.product.Product;
import com.es.phoneshop.model.viewHistory.ViewHistory;

import javax.servlet.http.HttpServletRequest;

public interface ViewHistoryService {
    void addToHistory(ViewHistory viewHistory, Product product);
    ViewHistory getViewHistory(HttpServletRequest request);
}

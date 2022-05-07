package com.es.phoneshop.service;

import com.es.phoneshop.model.product.Product;
import com.es.phoneshop.model.viewHistory.ViewHistory;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

public interface ViewHistoryService {
    void addToHistory(List<Product> viewHistory, Product product);

    ViewHistory getViewHistory(HttpServletRequest request);
}

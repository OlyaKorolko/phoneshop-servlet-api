package com.es.phoneshop.model.viewHistory;

import com.es.phoneshop.model.product.Product;

import java.util.ArrayList;
import java.util.List;

public class ViewHistory {
    private final List<Product> history;
    public static final int CAPACITY = 3;

    public ViewHistory() {
        this.history = new ArrayList<>(CAPACITY);
    }

    public List<Product> getHistory() {
        return history;
    }
}

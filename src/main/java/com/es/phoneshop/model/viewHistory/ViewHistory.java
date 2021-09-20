package com.es.phoneshop.model.viewHistory;

import com.es.phoneshop.model.product.Product;

import java.util.ArrayList;
import java.util.List;

public class ViewHistory {
    private final List<Product> history;
    private static final int CAPACITY = 3;

    public ViewHistory() {
        this.history = new ArrayList<>(CAPACITY);
    }

    public List<Product> getHistory() {
        return history;
    }

    public void addToHistory(Product product) {
        if (!history.contains(product)) {
            if (history.size() == CAPACITY) {
                history.remove(0);
            }
            history.add(product);
        } else if (history.size() > 1) {
            history.remove(product);
            history.add(product);
        }
    }
}

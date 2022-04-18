package com.es.phoneshop.model.viewHistory;

import com.es.phoneshop.model.product.Product;
import lombok.Getter;

import java.util.ArrayList;
import java.util.List;

@Getter
public class ViewHistory {
    private final List<Product> history;
    public static final int CAPACITY = 3;

    public ViewHistory() {
        this.history = new ArrayList<>(CAPACITY);
    }
}

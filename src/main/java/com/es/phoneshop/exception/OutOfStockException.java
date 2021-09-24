package com.es.phoneshop.exception;

import com.es.phoneshop.model.product.Product;

public class OutOfStockException extends Exception {
    private final Product product;
    private final int stockAvailable;
    private final int stockRequested;

    public OutOfStockException(Product product, int stockAvailable, int stockRequested) {
        this.product = product;
        this.stockAvailable = stockAvailable;
        this.stockRequested = stockRequested;
    }

    public Product getProduct() {
        return product;
    }

    public int getStockAvailable() {
        return stockAvailable;
    }

    public int getStockRequested() {
        return stockRequested;
    }
}

package com.es.phoneshop.exception;

import com.es.phoneshop.model.product.Product;

public class OutOfStockException extends Exception {

    public OutOfStockException(String message) {
        super(message);
    }

    public OutOfStockException(String message, Throwable cause) {
        super(message, cause);
    }
}

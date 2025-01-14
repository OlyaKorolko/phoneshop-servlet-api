package com.es.phoneshop.exception;

public class ProductNotFoundException extends BaseEntityNotFoundException {
    public ProductNotFoundException(String message) {
        super(message);
    }

    public ProductNotFoundException(String message, Throwable cause) {
        super(message, cause);
    }
}

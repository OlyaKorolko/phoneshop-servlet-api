package com.es.phoneshop.exception;

public class OrderNotFoundException extends BaseEntityNotFoundException {
    public OrderNotFoundException(String message) {
        super(message);
    }

    public OrderNotFoundException(String message, Throwable cause) {
        super(message, cause);
    }
}

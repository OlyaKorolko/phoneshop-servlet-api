package com.es.phoneshop.exception;

public class BaseEntityNotFoundException extends RuntimeException {
    public BaseEntityNotFoundException(String message) {
        super(message);
    }

    public BaseEntityNotFoundException(String message, Throwable cause) {
        super(message, cause);
    }
}

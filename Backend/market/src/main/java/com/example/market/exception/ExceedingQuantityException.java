package com.example.market.exception;

public class ExceedingQuantityException extends RuntimeException {
    public ExceedingQuantityException(String message) {
        super(message);
    }
}

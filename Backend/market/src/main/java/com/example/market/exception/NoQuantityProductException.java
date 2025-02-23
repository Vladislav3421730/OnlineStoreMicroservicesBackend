package com.example.market.exception;

public class NoQuantityProductException extends RuntimeException {
    public NoQuantityProductException(String message) {
        super(message);
    }
}

package com.example.market.exception;

public class ProductNotFoundException extends EntityNotFoundException {
    public ProductNotFoundException(String message) {
        super(message);
    }
}

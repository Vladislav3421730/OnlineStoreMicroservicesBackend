package com.example.market.exception;

public class OrderNotFoundException extends EntityNotFoundException {

    public OrderNotFoundException(String message) {
        super(message);
    }
}

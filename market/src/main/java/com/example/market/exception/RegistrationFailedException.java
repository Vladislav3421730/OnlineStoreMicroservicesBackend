package com.example.market.exception;

public class RegistrationFailedException extends RuntimeException {
    public RegistrationFailedException(String message) {
        super(message);
    }
}

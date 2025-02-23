package com.example.market.exception;

public class WrongIndexException extends RuntimeException {
    public WrongIndexException(String message) {
        super(message);
    }
}

package com.example.market.exception;

public class PasswordsNotTheSameException extends RuntimeException  {
    public PasswordsNotTheSameException(String message) {
        super(message);
    }
}

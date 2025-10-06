package com.example.shoppingflow.Exception;

public class ClientException extends RuntimeException {
    private final String message;

    public ClientException(String message) {
        super(message);
        this.message = message;
    }
}

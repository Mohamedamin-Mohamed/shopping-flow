package com.example.shoppingflow.DTO;

public class ErrorResponse {
    private String message;
    private String timestamp;
    private int status;
    private String error;

    public ErrorResponse(String message, String timestamp, int status, String error) {
        this.message = message;
        this.timestamp = timestamp;
        this.status = status;
        this.error = error;
    }
}

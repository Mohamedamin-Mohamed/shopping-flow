package com.example.shoppingflow.advice;

import com.example.shoppingflow.DTO.ErrorResponse;
import com.example.shoppingflow.Exception.ClientException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.time.LocalDateTime;

@ControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<ErrorResponse> handleIllegalArgumentException(IllegalArgumentException e) {
        return createResponse(e);
    }

    @ExceptionHandler(ClientException.class)
    public ResponseEntity<ErrorResponse> handleClientSideException(ClientException clientException) {
        return createResponse(clientException);
    }

    private ResponseEntity<ErrorResponse> createResponse(Exception e) {
        ErrorResponse errorResponse = buildErrorResponse(e);
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorResponse);
    }

    private ErrorResponse buildErrorResponse(Exception e) {
        return new ErrorResponse(e.getMessage(), LocalDateTime.now().toString(), 400, "Bad request");
    }


}

package com.example.swiftCodes.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class GlobalExceptionHandler {
    @ExceptionHandler(BankNotFoundException.class)
    public ResponseEntity<String> bankNotFoundException(String message) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(message);
    }

    @ExceptionHandler(BankAlreadyExistsException.class)
    public ResponseEntity<String> bankAlreadyExistsException(String message) {
        return ResponseEntity.status(HttpStatus.CONFLICT).body(message);
    }
}

package com.example.securitydemo.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.Optional;

import java.util.Map;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<Map<String, Object>> handleValidation(MethodArgumentNotValidException exception) {
        String message = Optional.ofNullable(exception.getBindingResult().getAllErrors())
            .flatMap(errors -> errors.stream().findFirst())
            .map(error -> error.getDefaultMessage())
            .orElse("Validation error");

        return ResponseEntity.badRequest().body(Map.of(
            "error", "VALIDATION_ERROR",
            "message", message
        ));
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<Map<String, Object>> handleGeneric(Exception exception) {
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(Map.of(
            "error", "INTERNAL_ERROR",
            "message", exception.getMessage()
        ));
    }
}

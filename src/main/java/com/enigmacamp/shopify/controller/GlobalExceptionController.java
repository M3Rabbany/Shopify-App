package com.enigmacamp.shopify.controller;

import com.enigmacamp.shopify.exception.ResourceNotFoundException;
import com.enigmacamp.shopify.exception.ValidationException;
import com.enigmacamp.shopify.model.CommonResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class GlobalExceptionController {
    @ExceptionHandler(ResourceNotFoundException.class)
    public ResponseEntity<CommonResponse<String>>
    handleNotFoundException(ResourceNotFoundException ex) {

        CommonResponse<String> response =
                CommonResponse.<String>builder()
                        .statusCode(HttpStatus.NOT_FOUND.value())
                        .message(ex.getMessage())
                        .build();

        return ResponseEntity
                .status(HttpStatus.NOT_FOUND)
                .body(response);
    }

    @ExceptionHandler(com.enigmacamp.shopify.exception.ValidationException.class)
    public ResponseEntity<CommonResponse<String>>
    handleValidationException(ValidationException ex) {
        CommonResponse<String> response =
                CommonResponse.<String>builder()
                        .statusCode(HttpStatus.NOT_ACCEPTABLE.value())
                        .message(ex.getMessage())
                        .build();

        return ResponseEntity
                .status(HttpStatus.NOT_ACCEPTABLE)
                .body(response);
    }
}


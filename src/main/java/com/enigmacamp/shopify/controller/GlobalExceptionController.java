package com.enigmacamp.shopify.controller;

import com.enigmacamp.shopify.exception.ForbiddenException;
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

    @ExceptionHandler(ValidationException.class)
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

    @ExceptionHandler(ForbiddenException.class)
    public ResponseEntity<CommonResponse<String>> handleForbiddenException(ForbiddenException e){
            CommonResponse<String> response = CommonResponse.<String>builder()
                    .statusCode(HttpStatus.FORBIDDEN.value())
                    .message(e.getMessage())
                    .build();

            return ResponseEntity.status(HttpStatus.FORBIDDEN)
                    .body(response);
    }
}


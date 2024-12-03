package com.enigmacamp.shopify.model;

import lombok.Builder;
import lombok.Data;

@Builder
@Data
public class CommonResponse<T> {
    private Integer statusCode;
    private String message;
    private T data;
}

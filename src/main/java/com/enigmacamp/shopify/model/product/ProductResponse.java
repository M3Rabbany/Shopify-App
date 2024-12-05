package com.enigmacamp.shopify.model.product;

import lombok.Builder;
import lombok.Data;

import java.util.Date;

@Data
@Builder
public class ProductResponse {
    private String id;
    private String name;
    private Long price;
    private Integer stock;
    private Date created;
}

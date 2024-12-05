package com.enigmacamp.shopify.service;

import com.enigmacamp.shopify.model.product.ProductRequest;
import com.enigmacamp.shopify.model.product.ProductResponse;

import java.util.List;

public interface ProductService {

    ProductResponse create(ProductRequest productRequest);
    ProductResponse getProductById(String productId);
    List<ProductResponse> getAllProducts(String name);
    ProductResponse update(ProductRequest productRequest);
    void updateStock(ProductRequest productRequest);
    void delete(String id);
}

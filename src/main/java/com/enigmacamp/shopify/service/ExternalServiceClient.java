package com.enigmacamp.shopify.service;

import com.enigmacamp.shopify.entity.Product;
import com.enigmacamp.shopify.model.payment.PaymentRequest;
import com.enigmacamp.shopify.model.payment.PaymentResponse;

import java.util.List;

public interface ExternalServiceClient {
    Product getProduct(String id);
    Product getProductWithErrorHandling(String id);
    PaymentResponse createPayment(PaymentRequest request);
    List<Product> searchProducts(String query,int page);
    void updateProduct(String id,Product product);
    void deleteProduct(String id);
}

package com.enigmacamp.shopify.service;

import com.enigmacamp.shopify.model.payment.PaymentRequest;
import com.enigmacamp.shopify.model.payment.PaymentResponse;

public interface PaymentService {
    PaymentResponse createPayment(PaymentRequest request);
}

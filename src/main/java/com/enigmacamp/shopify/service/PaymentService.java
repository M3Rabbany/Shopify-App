package com.enigmacamp.shopify.service;

import com.enigmacamp.shopify.entity.Payment;
import com.enigmacamp.shopify.entity.Transaction;
import com.enigmacamp.shopify.model.payment.PaymentResponse;
import com.enigmacamp.shopify.model.transaction.TransactionRequest;

public interface PaymentService {
    PaymentResponse createPayment(Transaction transaction);
}

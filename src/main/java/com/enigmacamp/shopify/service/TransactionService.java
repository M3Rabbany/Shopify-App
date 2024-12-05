package com.enigmacamp.shopify.service;

import com.enigmacamp.shopify.model.transaction.TransactionRequest;
import com.enigmacamp.shopify.model.transaction.TransactionResponse;

public interface TransactionService {
    TransactionResponse create(TransactionRequest request);
}

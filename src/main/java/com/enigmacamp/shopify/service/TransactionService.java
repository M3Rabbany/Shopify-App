package com.enigmacamp.shopify.service;

import com.enigmacamp.shopify.model.transaction.TransactionRequest;
import com.enigmacamp.shopify.model.transaction.TransactionResponse;

import java.util.List;

public interface TransactionService {
    TransactionResponse create(TransactionRequest request);
    void delete(String id);
    TransactionResponse getById(String id);
    List<TransactionResponse> getAll();
}

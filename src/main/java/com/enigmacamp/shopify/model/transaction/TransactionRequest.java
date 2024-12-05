package com.enigmacamp.shopify.model.transaction;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Builder
@NoArgsConstructor
@AllArgsConstructor
@Data
public class TransactionRequest {
    private String customerId;
    private List<TransactionDetailRequest> transactionDetailRequests;
}

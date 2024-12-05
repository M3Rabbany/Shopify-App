package com.enigmacamp.shopify.model.transaction;

import com.enigmacamp.shopify.entity.Customer;
import com.enigmacamp.shopify.entity.TransactionDetail;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;
import java.util.List;

@Builder
@NoArgsConstructor
@AllArgsConstructor
@Data
public class TransactionResponse {
    private String id;
    private Customer customer;
    private List<TransactionDetail> transactionDetails;
    private Date transactionDate;
    private Long totalPayment;
}
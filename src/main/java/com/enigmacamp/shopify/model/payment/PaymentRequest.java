package com.enigmacamp.shopify.model.payment;

import com.enigmacamp.shopify.entity.Customer;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class PaymentRequest {
    private String transactionId;
    private Customer customer;
}

package com.enigmacamp.shopify.controller;

import com.enigmacamp.shopify.entity.Transaction;
import com.enigmacamp.shopify.model.CommonResponse;
import com.enigmacamp.shopify.model.payment.PaymentResponse;
import com.enigmacamp.shopify.service.PaymentService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/payments")
public class PaymentController {
    private final PaymentService paymentService;

    @PostMapping
    public ResponseEntity<CommonResponse<PaymentResponse>> createPayment(@RequestBody Transaction transaction) {
        PaymentResponse paymentResponse = paymentService.createPayment(transaction);

        CommonResponse<PaymentResponse> response = CommonResponse.<PaymentResponse>builder()
                .data(paymentResponse)
                .message("Payment created successfully")
                .statusCode(HttpStatus.CREATED.value())
                .build();

        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }
}

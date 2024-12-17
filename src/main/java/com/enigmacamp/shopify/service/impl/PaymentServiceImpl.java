package com.enigmacamp.shopify.service.impl;

import com.enigmacamp.shopify.model.payment.PaymentRequest;
import com.enigmacamp.shopify.model.payment.PaymentResponse;
import com.enigmacamp.shopify.service.PaymentService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

import java.util.Base64;

@Service
@RequiredArgsConstructor
public class PaymentServiceImpl implements PaymentService {
    private final RestTemplate restTemplate;

    @Value("${midtrans.url}")
    private String midtransUrl;

    @Value("${midtrans.server.key}")
    private String serverKey;

    @Override
    public PaymentResponse createPayment(PaymentRequest request) {

        PaymentRequest paymentRequest = createPaymentRequest(request);

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.set("Authorization","Basic" + Base64.getEncoder().encodeToString(serverKey.getBytes()));

        HttpEntity<PaymentRequest> entity = new HttpEntity<>(paymentRequest, headers);

        try{
            ResponseEntity<PaymentResponse> response = restTemplate.postForEntity(midtransUrl, entity, PaymentResponse.class);
            return response.getBody();
        }catch (RestClientException e){
            throw new RuntimeException("Failed to create payment", e);
        }
    }

    private PaymentRequest createPaymentRequest(PaymentRequest request) {

        PaymentRequest paymentRequest = new PaymentRequest();
        paymentRequest.setTransactionId(request.getTransactionId());
        paymentRequest.setCustomerId(request.getCustomerId());

        return PaymentRequest.builder()
                .customerId(request.getCustomerId())
                .transactionId(request.getTransactionId())
                .build();
    }
}

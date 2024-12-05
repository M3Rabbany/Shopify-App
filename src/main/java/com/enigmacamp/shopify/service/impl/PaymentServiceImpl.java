package com.enigmacamp.shopify.service.impl;

import com.enigmacamp.shopify.entity.Transaction;
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

    @Value("${URL_MIDTRANS}")
    private String midtransUrl;

    @Value("${SERVER_KEY}")
    private String serverKey;

    @Override
    public PaymentResponse createPayment(Transaction transaction) {

        PaymentRequest paymentRequest = createPaymentRequest(transaction);

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

    private PaymentRequest createPaymentRequest(Transaction transaction) {

        PaymentRequest paymentRequest = new PaymentRequest();
        paymentRequest.setTransactionId(transaction.getId());
        paymentRequest.setCustomer(transaction.getCustomer());

        return PaymentRequest.builder()
                .customer(transaction.getCustomer())
                .transactionId(transaction.getId())
                .build();
    }
}

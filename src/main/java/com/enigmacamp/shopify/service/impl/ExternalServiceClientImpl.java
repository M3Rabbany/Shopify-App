package com.enigmacamp.shopify.service.impl;

import com.enigmacamp.shopify.entity.Product;
import com.enigmacamp.shopify.exception.ResourceNotFoundException;
import com.enigmacamp.shopify.model.payment.PaymentRequest;
import com.enigmacamp.shopify.model.payment.PaymentResponse;
import com.enigmacamp.shopify.service.ExternalServiceClient;
import com.enigmacamp.shopify.service.JwtService;
import lombok.RequiredArgsConstructor;
import org.hibernate.service.spi.ServiceException;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ExternalServiceClientImpl implements ExternalServiceClient {

    private final JwtService jwtService;
    private final RestTemplate restTemplate;
    private final String BASE_URL = "http://localhost:8080/api/external";

    @Override
    public Product getProduct(String id) {
        return restTemplate.getForObject(
                        BASE_URL + "/products/{id}",
                        Product.class,
                        id
                );
    }

    @Override
    public Product getProductWithErrorHandling(String id) {
        try {
            return restTemplate.getForObject(
                    BASE_URL + "/products/{id}",
                    Product.class,
                    id
            );
        } catch (HttpClientErrorException.NotFound e) {
            throw new ResourceNotFoundException(
                    "Product not found with id: " + id
            );
        } catch (RestClientException e) {
            throw new ServiceException(
                    "Error calling external service",
                    e
            );
        }
    }

    @Override
    public PaymentResponse createPayment(PaymentRequest request) {

        String token = jwtService.generateToken(request.getCustomer().getUserAccount());

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.set("Authorization", "Bearer " + token);
        HttpEntity<PaymentRequest> entity = new HttpEntity<>(request, headers);

        return restTemplate.patchForObject(
                BASE_URL + "/payments",
                entity,
                PaymentResponse.class
        );
    }

    @Override
    public List<Product> searchProducts(String query, int page) {
        String url = UriComponentsBuilder
                .fromHttpUrl(BASE_URL + "/products/search")
                .queryParam("q", query)
                .queryParam("page", page)
                .build()
                .toString();
        ResponseEntity<List<Product>> response = restTemplate.exchange(
                url,
                HttpMethod.GET,
                null,
                new ParameterizedTypeReference<>() {
                }
        );
        return response.getBody();
    }

    @Override
    public void updateProduct(String id, Product product) {
        restTemplate.put(
                BASE_URL + "/products/{id}",
                product,
                id
        );
    }

    @Override
    public void deleteProduct(String id) {
        restTemplate.delete(
                BASE_URL + "/products/{id}",
                id
        );
    }

}
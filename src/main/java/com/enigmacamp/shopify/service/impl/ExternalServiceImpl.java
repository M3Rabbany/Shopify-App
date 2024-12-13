package com.enigmacamp.shopify.service.impl;

import com.enigmacamp.shopify.model.RequestData;
import com.enigmacamp.shopify.model.ResponseData;
import com.enigmacamp.shopify.service.ExternalService;
import lombok.RequiredArgsConstructor;
import org.hibernate.service.spi.ServiceException;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClient;
import org.springframework.web.client.RestClientException;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ExternalServiceImpl implements ExternalService {
    private final RestClient restClient;

    @Override
    public ResponseData getData(String id) {
        try {
            return restClient.get()
                    .uri("https://jsonplaceholder.typicode.com/posts/{id}", id)
                    .retrieve()
                    .body(ResponseData.class);
        } catch (RestClientException e) {
            throw new ServiceException("Failed to get data", e);
        }
    }

    @Override
    public List<ResponseData> getAll() {
        try {
            return restClient.get()
                    .uri("https://jsonplaceholder.typicode.com/posts")
                    .retrieve()
                    .body((new ParameterizedTypeReference<>() {
                    }));
        } catch (RestClientException e) {
            throw new ServiceException("Failed to get all data", e);
        }
    }

    @Override
    public ResponseData create(RequestData request) {
        try {
            return restClient.post()
                    .uri("https://jsonplaceholder.typicode.com/posts")
                    .body(request)
                    .retrieve()
                    .body(ResponseData.class);
        } catch (RestClientException e) {
            throw new ServiceException("Failed to create data", e);
        }
    }

    @Override
    public ResponseData update(String id, RequestData request) {
        try {
            return restClient.put()
                    .uri("https://jsonplaceholder.typicode.com/posts/{id}", id)
                    .body(request)
                    .retrieve()
                    .body(ResponseData.class);
        } catch (RestClientException e) {
            throw new ServiceException("Failed to update data", e);
        }
    }

    @Override
    public void delete(String id) {
        try {
            restClient.delete()
                    .uri("https://jsonplaceholder.typicode.com/posts/{id}", id)
                    .retrieve()
                    .toBodilessEntity();
        } catch (RestClientException e) {
            throw new ServiceException("Failed to delete data", e);
        }
    }
}
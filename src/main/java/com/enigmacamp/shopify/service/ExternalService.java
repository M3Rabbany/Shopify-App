package com.enigmacamp.shopify.service;

import com.enigmacamp.shopify.model.RequestData;
import com.enigmacamp.shopify.model.ResponseData;

import java.util.List;

public interface ExternalService {
    ResponseData getData (String id);
    List<ResponseData> getAll();
    ResponseData create(RequestData request);
    ResponseData update(String id, RequestData request);
    void delete(String id);
}

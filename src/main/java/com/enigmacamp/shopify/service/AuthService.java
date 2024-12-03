package com.enigmacamp.shopify.service;

import com.enigmacamp.shopify.model.customer.CustomerRequest;
import com.enigmacamp.shopify.model.user.RegisterResponse;

public interface AuthService {
    RegisterResponse registerCustomer(CustomerRequest request);
}

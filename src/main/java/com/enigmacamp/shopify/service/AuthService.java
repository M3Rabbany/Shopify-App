package com.enigmacamp.shopify.service;

import com.enigmacamp.shopify.model.customer.CustomerRequest;
import com.enigmacamp.shopify.model.user.AuthRequest;
import com.enigmacamp.shopify.model.user.LoginResponse;
import com.enigmacamp.shopify.model.user.RegisterResponse;

public interface AuthService {
    RegisterResponse register(CustomerRequest request);
    LoginResponse login(AuthRequest request);
}

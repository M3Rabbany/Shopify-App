package com.enigmacamp.shopify.service.impl;

import com.enigmacamp.shopify.entity.UserAccount;
import com.enigmacamp.shopify.model.customer.CustomerRequest;
import com.enigmacamp.shopify.model.user.RegisterResponse;
import com.enigmacamp.shopify.service.AuthService;
import com.enigmacamp.shopify.service.CustomerService;
import com.enigmacamp.shopify.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class AuthServiceImpl implements AuthService {
    private final CustomerService customerService;
    private final UserService userService;

    @Override
    @Transactional(rollbackFor = Exception.class)
    public RegisterResponse registerCustomer(CustomerRequest request) {
        UserAccount account = UserAccount.builder()
                .username(request.getUsername())
                .password(request.getPassword())
                .build();
        userService.create(account);

        CustomerRequest customer = CustomerRequest.builder()
                .name(request.getName())
                .email(request.getEmail())
                .phone(request.getPhone())
                .address(request.getAddress())
                .build();

        customerService.createCustomer(customer);

        return RegisterResponse.builder()
                .username(request.getUsername())
                .build();
    }
}

package com.enigmacamp.shopify.service.impl;


import com.enigmacamp.shopify.constant.UserRole;
import com.enigmacamp.shopify.entity.Customer;
import com.enigmacamp.shopify.entity.Role;
import com.enigmacamp.shopify.entity.UserAccount;
import com.enigmacamp.shopify.exception.ForbiddenException;
import com.enigmacamp.shopify.model.customer.CustomerRequest;
import com.enigmacamp.shopify.model.user.AuthRequest;
import com.enigmacamp.shopify.model.user.LoginResponse;
import com.enigmacamp.shopify.model.user.RegisterResponse;
import com.enigmacamp.shopify.repository.RoleRepository;
import com.enigmacamp.shopify.service.AuthService;
import com.enigmacamp.shopify.service.CustomerService;
import com.enigmacamp.shopify.service.JwtService;
import com.enigmacamp.shopify.service.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.time.Instant;
import java.util.Collections;
import java.util.Date;


@Service
@RequiredArgsConstructor
@Slf4j
public class AuthServiceImpl implements AuthService {

    private final CustomerService customerService;
    private final RoleRepository roleRepository;
    private final UserService userService;
    private final AuthenticationManager authenticationManager;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;

    @Override
    @Transactional(rollbackFor = Exception.class)
    public RegisterResponse register(CustomerRequest request) {

        UserRole userRole;
        try {
            userRole = UserRole.valueOf(request.getRole());
        } catch (IllegalArgumentException e) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Invalid role provided");
        }

        Role role = Role.builder()
                .role(userRole)
                .build();
        roleRepository.save(role);

        // TODO: insert user account
        UserAccount account = UserAccount.builder()
                .username(request.getUsername())
                .password(passwordEncoder.encode(request.getPassword()))
                .role(Collections.singletonList(role))
                .build();

        account = userService.create(account);

        // TODO: insert customer
        Customer customer = Customer.builder()
                .name(request.getName())
                .address(request.getAddress())
                .email(request.getEmail())
                .phone(request.getPhone())
                .userAccount(account)
                .createAt(Date.from(Instant.now()))
                .build();

        customerService.createCustomer(customer);

        log.info("Email : {}",request.getEmail());

        return RegisterResponse.builder()
                .username(request.getUsername())
                .roles(request.getRole())
                .build();
    }

    @Override
    public LoginResponse login(AuthRequest request) {

        try {
            Authentication authentication = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(
                            request.getUsername(),
                            request.getPassword()
                    )
            );

            // Set authentication to context
            SecurityContextHolder.getContext().setAuthentication(authentication);

            // Generate Token
            UserAccount user = (UserAccount) authentication.getPrincipal();
            String token = jwtService.generateToken(user);

            return LoginResponse.builder()
                    .username(request.getUsername())
                    .token(token)
                    .roles(user.getRole())
                    .build();
        }catch (BadCredentialsException e){
         throw new ForbiddenException("Invalid username or password");
        }
    }
}


package com.enigmacamp.shopify.controller;

import com.enigmacamp.shopify.constant.UserRole;
import com.enigmacamp.shopify.exception.ForbiddenException;
import com.enigmacamp.shopify.model.CommonResponse;
import com.enigmacamp.shopify.model.customer.CustomerRequest;
import com.enigmacamp.shopify.model.user.AuthRequest;
import com.enigmacamp.shopify.model.user.LoginResponse;
import com.enigmacamp.shopify.model.user.RegisterRequest;
import com.enigmacamp.shopify.model.user.RegisterResponse;
import com.enigmacamp.shopify.service.AuthService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/auth")
@RequiredArgsConstructor
public class AuthController {
    private final AuthService authService;

    @PostMapping("/register")
    public ResponseEntity<CommonResponse<RegisterResponse>> register(@RequestBody CustomerRequest request) {
        RegisterResponse response = authService.register(request);

        CommonResponse<RegisterResponse> commonResponse = CommonResponse.<RegisterResponse>builder()
                .statusCode(HttpStatus.CREATED.value())
                .message("Successfully registered")
                .data(response)
                .build();

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(commonResponse);
    }

    @PostMapping("/login")
    public ResponseEntity<CommonResponse<LoginResponse>> login(@RequestBody AuthRequest request) {
        LoginResponse response = authService.login(request);

        CommonResponse<LoginResponse> commonResponse = CommonResponse.<LoginResponse>builder()
                .statusCode(HttpStatus.OK.value())
                .message("Successfully login")
                .data(response)
                .build();

        return ResponseEntity
                .status(HttpStatus.OK)
                .body(commonResponse);
    }
}

package com.enigmacamp.shopify.service.impl;

import com.enigmacamp.shopify.constant.UserRole;
import com.enigmacamp.shopify.entity.Role;
import com.enigmacamp.shopify.entity.UserAccount;
import com.enigmacamp.shopify.exception.ForbiddenException;
import com.enigmacamp.shopify.model.user.AuthRequest;
import com.enigmacamp.shopify.model.user.LoginResponse;
import com.enigmacamp.shopify.model.user.RegisterRequest;
import com.enigmacamp.shopify.model.user.RegisterResponse;
import com.enigmacamp.shopify.service.AuthService;
import com.enigmacamp.shopify.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collections;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class AuthServiceImpl implements AuthService {
    private final UserService userService;
    private final AuthenticationManager authenticationManager;
    private final PasswordEncoder passwordEncoder;

    @Override
    @Transactional(rollbackFor = Exception.class)
    public RegisterResponse register(RegisterRequest request) {

        Role role = Role.builder()
                .id(UUID.randomUUID().toString())
                .roles(parseUserRole(request.getRole()))
                .build();

        UserAccount account = UserAccount.builder()
                .id(UUID.randomUUID().toString())
                .username(request.getUsername())
                .password(passwordEncoder.encode(request.getPassword()))
                .role(Collections.singletonList(role))
                .build();
        try {
            userService.create(account);
        }catch (Exception e){
            throw new ForbiddenException("Undocumented!");
        }

        return RegisterResponse.builder()
                .username(request.getUsername())
                .roles(String.valueOf(request.getRole()))
                .build();
    }

    @Override
    public LoginResponse login(AuthRequest request) {

        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        request.getUsername(),
                        request.getPassword()
                )
        );

        SecurityContextHolder.getContext().setAuthentication(authentication);
        UserAccount user = (UserAccount) authentication.getPrincipal();
        String token = "AEZAKMI92";

        return LoginResponse.builder()
                .username(request.getUsername())
                .roles(user.getRole())
                .token(token)
                .build();
    }

    private UserRole parseUserRole(String role) {
        try {
            return UserRole.valueOf(role.toUpperCase());
        } catch (IllegalArgumentException e) {
            throw new ForbiddenException("Invalid role provided: " + role);
        }
    }

}

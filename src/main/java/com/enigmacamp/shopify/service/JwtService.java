package com.enigmacamp.shopify.service;

import com.enigmacamp.shopify.entity.UserAccount;
import com.enigmacamp.shopify.model.user.JwtClaims;

public interface JwtService {
    String generateToken(UserAccount userAccount);
    boolean verifyJwtToken(String token);
    JwtClaims getJwtClaims(String token);
}

package com.enigmacamp.shopify.service.impl;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTCreationException;
import com.auth0.jwt.exceptions.JWTVerificationException;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.enigmacamp.shopify.entity.UserAccount;
import com.enigmacamp.shopify.model.user.JwtClaims;
import com.enigmacamp.shopify.service.JwtService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.time.Instant;

@Service
public class JwtServiceImpl implements JwtService {

    private static final Logger log = LoggerFactory.getLogger(JwtServiceImpl.class);

    @Override
    public String generateToken(UserAccount userAccount) {
        Algorithm algorithm = Algorithm.HMAC256("SECRETKEY");
        try {
            return JWT.create()
                    .withSubject(userAccount.getId())
                    .withClaim("role", userAccount.getAuthorities().stream().map(GrantedAuthority::getAuthority).toList())
                    .withIssuedAt(Instant.now())
                    .withExpiresAt(Instant.now().plusSeconds(3600))
                    .withIssuer("SHOPIFY APP")
                    .sign(algorithm);
        } catch (JWTCreationException e) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Failed to create JWT token", e);
        }
    }

    @Override
    public boolean verifyJwtToken(String token) {
        try {
            Algorithm algorithm = Algorithm.HMAC256("SECRETKEY");
            JWTVerifier jwtVerifier = JWT.require(algorithm).withIssuer("SHOPIFY APP").build();
            log.info("Token: {}", token);
            jwtVerifier.verify(parseJwt(token));
            return true;
        } catch (JWTVerificationException e) {
            log.error("Failed to verify JWT token", e);
            return false;
        }
    }

    @Override
    public JwtClaims getJwtClaims(String token) {
        try {
            Algorithm algorithm = Algorithm.HMAC256("SECRETKEY");
            JWTVerifier jwtVerifier = JWT.require(algorithm).withIssuer("SHOPIFY APP").build();
            DecodedJWT decodeJwt = jwtVerifier.verify(parseJwt(token));
            return JwtClaims.builder()
                    .userAccountId(decodeJwt.getSubject())
                    .roles(decodeJwt.getClaim("role").asList(String.class))
                    .build();
        } catch (JWTVerificationException e) {
            log.error("Invalid JWT token", e);
            return null;
        }
    }

    private String parseJwt(String token) {
        if (token != null && token.startsWith("Bearer ")) {
            return token.substring(7);
        }
        return null;
    }
}

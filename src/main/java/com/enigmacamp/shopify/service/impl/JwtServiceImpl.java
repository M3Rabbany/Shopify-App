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
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.time.Instant;
import java.util.List;

@Slf4j
@Service
public class JwtServiceImpl implements JwtService {
    @Override
    public String generateToken(UserAccount userAccount) {
        Algorithm algorithm = Algorithm.HMAC256("IBNURABBANI");
        try {
            return JWT.create()
                    .withSubject(userAccount.getId())
                    .withClaim("role",userAccount.getAuthorities().stream().map(GrantedAuthority::getAuthority).toList())
                    .withIssuedAt(Instant.now())
                    .withExpiresAt(Instant.now().plusSeconds(3360))
                    .withIssuer("SHOPIFY APP")
                    .sign(algorithm);
        }catch (JWTCreationException e){
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR,"Failed to create JWT token",e);
        }
    }

    @Override
    public boolean verifyJwtToken(String token) {
        try {
            Algorithm algorithm = Algorithm.HMAC256("IBNURABBANI");
            JWTVerifier jwtVerifier = JWT.require(algorithm).withIssuer("SHOPIFY APP").build();
            log.info("Token: {}",token);
            jwtVerifier.verify(parseJwt(token));
            return true;
        }catch (JWTVerificationException e){
            return false;
        }
    }

    @Override
    public JwtClaims getJwtClaims(String token) {
        try {
            Algorithm algorithm = Algorithm.HMAC256("IBNURABBANI");
            JWTVerifier jwtVerifier = JWT.require(algorithm).withIssuer("SHOPIFY APP").build();
            DecodedJWT decodedJWT = jwtVerifier.verify(parseJwt(token));

            return JwtClaims.builder()
                    .userAccountId(decodedJWT.getSubject())
                    .roles(decodedJWT.getClaim("role").asList(String.class))
                    .build();
        }catch (JWTVerificationException e){
            throw new ResponseStatusException((HttpStatus.UNAUTHORIZED),"Invalid JWT Token" + e);
        }
    }

    private String parseJwt(String token){
        if (token != null && token.startsWith("Bearer ")) {
            return token.substring(7);
        }
        return null;
    }
}

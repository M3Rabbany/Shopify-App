package com.enigmacamp.shopify.service;

import com.enigmacamp.shopify.entity.UserAccount;
import org.springframework.security.core.userdetails.UserDetailsService;

public interface UserService extends UserDetailsService {
    UserAccount create(UserAccount user);
}

package com.enigmacamp.shopify.service.impl;

import com.enigmacamp.shopify.entity.UserAccount;
import com.enigmacamp.shopify.repository.UserRepository;
import com.enigmacamp.shopify.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {
    private final UserRepository userRepository;


    @Override
    public UserAccount create(UserAccount user) {
        return userRepository.saveAndFlush(user);
    }
}

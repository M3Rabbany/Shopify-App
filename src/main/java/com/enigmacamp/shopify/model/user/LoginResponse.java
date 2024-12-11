package com.enigmacamp.shopify.model.user;

import com.enigmacamp.shopify.constant.UserRole;
import com.enigmacamp.shopify.entity.Role;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class LoginResponse {
    private String username;
    private String token;
    private List<Role> roles;
}

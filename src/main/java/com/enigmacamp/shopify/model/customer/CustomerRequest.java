package com.enigmacamp.shopify.model.customer;

import com.fasterxml.jackson.annotation.JsonAlias;
import jakarta.persistence.Column;
import jakarta.validation.constraints.Pattern;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Builder
@Data
@AllArgsConstructor
@NoArgsConstructor
public class CustomerRequest {

    private String username;

    private String password;

    @JsonAlias("full_name")
    @Column(unique = true)
    private String name;

    @Column(unique = true)
    private String email;

    @JsonAlias("phone_number")
    @Column(unique = true)
    private String phone;

    private String address;

    @Pattern(regexp = "ADMIN|SELLER|CUSTOMER|SUPER_ADMIN", message = "Invalid role provided")
    private String role;
}

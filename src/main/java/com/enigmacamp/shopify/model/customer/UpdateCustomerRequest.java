package com.enigmacamp.shopify.model.customer;

import com.enigmacamp.shopify.entity.Image;
import jakarta.persistence.Column;
import jakarta.validation.constraints.NotNull;
import lombok.*;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class UpdateCustomerRequest {

    @NotNull
    private String id;

    @Column(unique = true)
    private String name;

    @Column(unique = true)
    private String email;

    @Column(unique = true)
    private String phone;

    private String address;

    private Image image;
}

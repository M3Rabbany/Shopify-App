package com.enigmacamp.shopify.model.customer;

import com.enigmacamp.shopify.entity.Image;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class CustomerResponse {

    private String id;
    private String name;
    private String email;
    private String phone;
    private String address;
    private Image image;
    private Date createdAt;
}

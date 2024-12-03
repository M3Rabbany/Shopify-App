package com.enigmacamp.shopify.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@NoArgsConstructor
@AllArgsConstructor
@Data
@Table(name = "m_payment")
public class Payment {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private String id;

    @Column(name = "redirect_url", nullable = false)
    private String redirectUrl;

    @Column(nullable = false)
    private String method;

    @OneToOne(mappedBy = "payment")
    private Transaction transaction;
}

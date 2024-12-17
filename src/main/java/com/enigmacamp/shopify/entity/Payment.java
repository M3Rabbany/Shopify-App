package com.enigmacamp.shopify.entity;

import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;
import lombok.*;
import org.springframework.beans.factory.annotation.Value;

@Entity
@NoArgsConstructor
@AllArgsConstructor
@Data
@Builder
@Table(name = "m_payment")
public class Payment {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private String id;

    @Column(name = "redirect_url", nullable = false)
    @Value("${midtrans.url}")
    private String redirectUrl;

    @Column(nullable = false)
    private String method;

    @Column(nullable = false)
    private Long totalPayments;

    @OneToOne(mappedBy = "payment")
    @JsonBackReference
    private Transaction transaction;
}

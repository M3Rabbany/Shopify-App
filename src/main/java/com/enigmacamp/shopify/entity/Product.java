package com.enigmacamp.shopify.entity;

import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
@Table(name = "m_product")
@NoArgsConstructor
@AllArgsConstructor
@Entity
public class Product {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private String id;
    @Column(nullable = false, unique = true)
    private String name;
    @Column(nullable = false, columnDefinition = "BIGINT CHECK (price >= 0)")
    private Long price;
    @Column(nullable = false, columnDefinition = "BIGINT CHECK (stock >= 0)")
    private Integer stock;
    @Column(
            name = "create_at"
    )
    @Temporal(TemporalType.DATE)
    @JsonFormat(pattern = "yyyy-MM-dd")
    private Date createAt;
}

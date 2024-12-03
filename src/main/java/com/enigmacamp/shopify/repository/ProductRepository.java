package com.enigmacamp.shopify.repository;

import com.enigmacamp.shopify.entity.Product;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ProductRepository extends JpaRepository<Product, String> {

    boolean existsByName(String name);

    Optional<Product> findFirstById(String idProduct);

    List<Product> findAllByNameLikeOrderByNameDesc(String name);
}

package com.enigmacamp.shopify.service.impl;

import com.enigmacamp.shopify.entity.Product;
import com.enigmacamp.shopify.exception.ValidationException;
import com.enigmacamp.shopify.model.product.ProductRequest;
import com.enigmacamp.shopify.model.product.ProductResponse;
import com.enigmacamp.shopify.repository.ProductRepository;
import com.enigmacamp.shopify.service.ProductService;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validator;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.Set;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class ProductServiceImpl implements ProductService {
    private final ProductRepository productRepository;
    private final Validator validator;

    @Override
    @Transactional
    public ProductResponse create(ProductRequest payload) {

        Set<ConstraintViolation<ProductRequest>> violations = validator.validate(payload);
        if (!violations.isEmpty()) {
            throw new ValidationException(violations.iterator().next().getMessage(), null);
        }

        if (productRepository.existsByName(payload.getName())) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, "Product already exists");
        }

        Product product = new Product();
        product.setId(UUID.randomUUID().toString());
        product.setName(payload.getName());
        product.setPrice(payload.getPrice());
        product.setStock(payload.getStock());
        productRepository.save(product);

        return toProductResponse(product);
    }

    private ProductResponse toProductResponse(Product product) {
        return ProductResponse.builder()
                .id(product.getId())
                .name(product.getName())
                .price(product.getPrice())
                .stock(product.getStock())
                .build();
    }

    @Override
    @Transactional(readOnly = true)
    public ProductResponse getProductById(String productId) {
        Product product = productRepository.findById(productId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Product not found"));
        return toProductResponse(product);
    }

    @Override
    public List<ProductResponse> getAllProducts(String name) {

        if (name != null) {
            return productRepository.findAllByNameLikeOrderByNameDesc("%" + name + "%").stream()
                    .map(this::toProductResponse).toList();
        }

        List<ProductResponse> products = productRepository.findAll().stream()
                .map(this::toProductResponse).toList();

        if (products.isEmpty()) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND,"Product not found!");
        }

        return products;
    }

    @Override
    @Transactional
    public ProductResponse update(ProductRequest request) {

        Set<ConstraintViolation<ProductRequest>> violations = validator.validate(request);
        if (!violations.isEmpty()) {
            throw new ValidationException(violations.iterator().next().getMessage(), null);
        }

        Product product = productRepository.findFirstById(request.getId())
                        .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Product not found"));

        product.setName(request.getName());
        product.setPrice(request.getPrice());
        product.setStock(request.getStock());
        productRepository.save(product);

        return toProductResponse(product);
    }

    @Override
    @Transactional
    public void delete(String id) {
        Product product = productRepository.findFirstById(id)
                        .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Product not found"));

        productRepository.delete(product);
    }
}

package com.enigmacamp.shopify.service.impl;

import com.enigmacamp.shopify.entity.Product;
import com.enigmacamp.shopify.model.product.ProductRequest;
import com.enigmacamp.shopify.model.product.ProductResponse;
import com.enigmacamp.shopify.repository.ProductRepository;
import com.enigmacamp.shopify.service.ProductService;
import lombok.RequiredArgsConstructor;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.client.MultipartBodyBuilder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.RestClient;
import org.springframework.web.server.ResponseStatusException;

import java.time.Instant;
import java.util.Date;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class ProductServiceImpl implements ProductService {
    private final ProductRepository productRepository;
    private final ValidatorService validatorService;

    @Override
    @Transactional
    public ProductResponse create(ProductRequest request) {
        validatorService.validate(request);

        if (productRepository.existsByName(request.getName())) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, "Product already exists");
        }

        Product product = new Product();
        product.setId(UUID.randomUUID().toString());
        product.setName(request.getName());
        product.setPrice(request.getPrice());
        product.setStock(request.getStock());
        product.setCreateAt(Date.from(Instant.now()));
        productRepository.save(product);

        return toProductResponse(product);
    }

    private ProductResponse toProductResponse(Product product) {
        return ProductResponse.builder()
                .id(product.getId())
                .name(product.getName())
                .price(product.getPrice())
                .stock(product.getStock())
                .created(product.getCreateAt())
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
        validatorService.validate(request);

        Product product = productRepository.findFirstById(request.getId())
                        .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Product not found"));

        product.setName(request.getName());
        product.setPrice(request.getPrice());
        product.setStock(request.getStock());
        productRepository.save(product);

        return toProductResponse(product);
    }

    @Override
    public void updateStock(ProductRequest request) {
        validatorService.validate(request);

        Product product = productRepository.findById(request.getId())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Product not found"));

        product.setStock(request.getStock());
        productRepository.save(product);
        toProductResponse(product);
    }

    @Override
    @Transactional
    public void delete(String id) {
        Product product = productRepository.findFirstById(id)
                        .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Product not found"));

        productRepository.delete(product);
    }
}

package com.enigmacamp.shopify.controller;

import com.enigmacamp.shopify.entity.Product;
import com.enigmacamp.shopify.model.CommonResponse;
import com.enigmacamp.shopify.model.product.ProductRequest;
import com.enigmacamp.shopify.model.product.ProductResponse;
import com.enigmacamp.shopify.repository.ProductRepository;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.*;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@RequiredArgsConstructor
class ProductControllerTest {
    private final MockMvc mockMvc;
    private final ObjectMapper objectMapper;
    private final ProductRepository productRepository;

    @BeforeEach
    void setUp() {
        productRepository.deleteAll();
    }

    @Test
    void createProduct() throws Exception {
        ProductRequest request = new ProductRequest();
        request.setName("Laptop");
        request.setPrice(20000000L);
        request.setStock(5);

        mockMvc.perform(
                post("/api/v1/product")
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request))
        ).andExpectAll(
                status().isCreated()
        ).andDo(result -> {
            CommonResponse<ProductResponse> response = objectMapper.readValue(result.getResponse().getContentAsString(), new TypeReference<>() {
            });
            assertNotNull(response.getData());
            assertEquals("Laptop", response.getData().getName());
            assertEquals(20000000L, response.getData().getPrice());
            assertEquals(5, response.getData().getStock());
        });
    }

    @Test
    void getProductById() throws Exception {
        Product product = new Product();
        product.setName("Laptop");
        product.setPrice(20000000L);
        product.setStock(5);
        productRepository.save(product);

        mockMvc.perform(
                get("/api/v1/product/" + product.getId())
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
        ).andExpectAll(
                status().isOk()
        ).andDo(result -> {
            CommonResponse<ProductResponse> response = objectMapper.readValue(result.getResponse().getContentAsString(), new TypeReference<>() {
            });
            assertNotNull(response.getData());
            assertEquals("Laptop", response.getData().getName());
            assertEquals(20000000L, response.getData().getPrice());
            assertEquals(5, response.getData().getStock());
        });
    }

    @Test
    void getAllProduct() throws Exception {

        for (int i = 0; i < 3; i++) {
            Product product = new Product();
            product.setId(UUID.randomUUID().toString());
            product.setName("Laptop" + i);
            product.setPrice(20000000L);
            product.setStock(5);
            productRepository.save(product);
        }

        mockMvc.perform(
                get("/api/v1/product")
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
        ).andExpectAll(
                status().isOk()
        ).andDo(result -> {
            List<ProductResponse> response = objectMapper.readValue(result.getResponse().getContentAsString(), new TypeReference<>() {
            });
            assertNotNull(response);
            assertEquals(3, response.size());
        });
    }

    @Test
    void updateProductById() throws Exception {
        Product product = new Product();
        product.setName("Laptop");
        product.setPrice(20000000L);
        product.setStock(5);
        productRepository.save(product);

        ProductRequest request = new ProductRequest();
        request.setId(product.getId());
        request.setName("Handphone");
        request.setPrice(3000000L);
        request.setStock(5);

        mockMvc.perform(
                put("/api/v1/product/" + product.getId())
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request))
        ).andExpectAll(
                status().isOk()
        ).andDo(result -> {
            CommonResponse<ProductResponse> response = objectMapper.readValue(result.getResponse().getContentAsString(), new TypeReference<>() {
            });
            assertNotNull(response.getData());
            assertEquals("Handphone", response.getData().getName());
            assertEquals(3000000L, response.getData().getPrice());
            assertEquals(5, response.getData().getStock());
        });
    }

    @Test
    void deleteProductById() throws Exception {
        Product product = new Product();
        product.setId(product.getId());
        product.setName("Laptop");
        product.setPrice(20000000L);
        product.setStock(5);
        productRepository.save(product);

        mockMvc.perform(
                delete("/api/v1/product/" + product.getId())
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(product))
        ).andExpectAll(
                status().isOk()
        ).andDo(result -> {
            CommonResponse<ProductResponse> response = objectMapper.readValue(result.getResponse().getContentAsString(), new TypeReference<>() {
            });
            assertNull(response.getData());
        });
    }
}
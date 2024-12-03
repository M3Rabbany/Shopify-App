package com.enigmacamp.shopify.controller;

import com.enigmacamp.shopify.entity.Customer;
import com.enigmacamp.shopify.model.CommonResponse;
import com.enigmacamp.shopify.model.customer.CustomerRequest;
import com.enigmacamp.shopify.model.customer.CustomerResponse;
import com.enigmacamp.shopify.model.customer.UpdateCustomerRequest;
import com.enigmacamp.shopify.repository.CustomerRepository;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import java.time.Instant;
import java.util.Date;
import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@RequiredArgsConstructor
class CustomerControllerTest {
    private final CustomerRepository customerRepository;
    private final MockMvc mockMvc;
    private final ObjectMapper objectMapper;

    @BeforeEach
    void setUp() {
        customerRepository.deleteAll();
    }

    @Test
    void createCustomer() throws Exception {
        CustomerRequest request = new CustomerRequest();
        request.setName("Test");
        request.setEmail("test@test.com");
        request.setPhone("374274685");
        request.setAddress("Test");

        mockMvc.perform(
                post("/api/v1/customers")
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request))
        ).andExpectAll(
                status().isCreated()
        ).andDo(result -> {
            CommonResponse<CustomerResponse> response = objectMapper.readValue(result.getResponse().getContentAsString(), new TypeReference<>() {
            });
            assertNotNull(response.getData());
            assertEquals("Test", response.getData().getName());
            assertEquals("test@test.com", response.getData().getEmail());
            assertEquals("374274685", response.getData().getPhone());
            assertEquals("Test", response.getData().getAddress());
            assertNull(response.getData().getImage());
        });
    }

    @Test
    void updateCustomerById() throws Exception {
        Customer customer = new Customer();
        customer.setId(UUID.randomUUID().toString());
        customer.setName("Test");
        customer.setEmail("test@test.com");
        customer.setPhone("374274685");
        customer.setAddress("Test");
        customer.setCreateAt(Date.from(Instant.now()));
        customer.setImage(null);
        customerRepository.save(customer);

        UpdateCustomerRequest request = new UpdateCustomerRequest();
        request.setId(customer.getId());
        request.setName("Ibnu");
        request.setEmail("ibnu@gmail.com");
        request.setPhone("374274685");
        request.setAddress("Jakarta");

        mockMvc.perform(
                put("/api/v1/customers/" + customer.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .accept(MediaType.APPLICATION_JSON)
                    .content(objectMapper.writeValueAsString(request))
        ).andExpectAll(
                status().isOk()
        ).andDo(result -> {
            CommonResponse<CustomerResponse> response = objectMapper.readValue(result.getResponse().getContentAsString(), new TypeReference<>() {
            });
            assertNotNull(response.getData());
            assertEquals("Ibnu", response.getData().getName());
            assertEquals("ibnu@gmail.com", response.getData().getEmail());
            assertEquals("374274685", response.getData().getPhone());
            assertEquals("Jakarta", response.getData().getAddress());
        });
    }

    @Test
    void deleteCustomerById() throws Exception {
        Customer customer = new Customer();
        customer.setId(UUID.randomUUID().toString());
        customer.setName("Test");
        customer.setEmail("test@test.com");
        customer.setPhone("374274685");
        customer.setAddress("Test");
        customer.setCreateAt(customer.getCreateAt());
        customer.setImage(null);
        customerRepository.save(customer);

        mockMvc.perform(
                MockMvcRequestBuilders.delete("/api/v1/customers/" + customer.getId())
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(customer))
        ).andExpectAll(
                status().isOk()
        ).andDo(result -> {
            CommonResponse<CustomerResponse> response = objectMapper.readValue(result.getResponse().getContentAsString(), new TypeReference<>() {
            });
            assertNull(response.getData());
        });
    }

    @Test
    void getAllCustomer() throws Exception {
        for (int i = 0; i < 3; i++) {
            Customer customer = new Customer();
            customer.setId(UUID.randomUUID().toString());
            customer.setName("Ibnu" + i);
            customer.setEmail("ibnu" + i + "@gmail.com");
            customer.setPhone("374274685" + i);
            customer.setAddress("Jakarta");
            customer.setCreateAt(customer.getCreateAt());
            customer.setImage(null);
            customerRepository.save(customer);
        }
        mockMvc.perform(
                get("/api/v1/customers")
        ).andExpectAll(
                status().isOk()
        ).andDo(result -> {
            List<CustomerResponse> response = objectMapper.readValue(result.getResponse().getContentAsString(), new TypeReference<>() {
            });
            assertNotNull(response);
            assertEquals(3, response.size());
        });
    }

    @Test
    void searchCustomerByQuery() throws Exception {

        for (int i = 0; i < 3; i++) {
            Customer customer = new Customer();
            customer.setId(UUID.randomUUID().toString());
            customer.setName("Ibnu" + i);
            customer.setEmail("ibnu" + i + "@gmail.com");
            customer.setPhone("374274685" + i);
            customer.setAddress("Jakarta");
            customer.setCreateAt(customer.getCreateAt());
            customer.setImage(null);
            customerRepository.save(customer);
        }

//        USING FIRST NAME
        mockMvc.perform(
                get("/api/v1/customers/search")
                        .queryParam("query", "Ibnu")
                        .accept(MediaType.APPLICATION_JSON)
                        .contentType(MediaType.APPLICATION_JSON)
        ).andExpectAll(
                status().isOk()
        ).andDo(result -> {
            List<CustomerResponse> response = objectMapper.readValue(result.getResponse().getContentAsString(), new TypeReference<>() {
            });
            assertNotNull(response);
            assertEquals(3, response.size());
        });
    }
}
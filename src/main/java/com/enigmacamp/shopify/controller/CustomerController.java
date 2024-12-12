package com.enigmacamp.shopify.controller;

import com.enigmacamp.shopify.entity.Customer;
import com.enigmacamp.shopify.model.CommonResponse;
import com.enigmacamp.shopify.model.customer.CustomerResponse;
import com.enigmacamp.shopify.model.customer.UpdateCustomerRequest;
import com.enigmacamp.shopify.service.impl.CustomerServiceImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@RestController
@RequestMapping("/api/v1/customers")
@RequiredArgsConstructor
public class CustomerController {
    private final CustomerServiceImpl customerService;

    @GetMapping("/search")
    public List<Customer> searchCustomer(@RequestParam String query) {
        return customerService.searchCustomer(query);
    }

    @PostMapping
    public ResponseEntity<CommonResponse<CustomerResponse>> createCustomer(@RequestBody Customer customer) {
        CustomerResponse customerResponse = customerService.createCustomer(customer);

        CommonResponse<CustomerResponse> response = CommonResponse.<CustomerResponse>builder()
                .statusCode(HttpStatus.CREATED.value())
                .message("Customer created")
                .data(customerResponse)
                .build();

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(response);
    }

    @PutMapping("/{id}")
    public ResponseEntity<CommonResponse<CustomerResponse>> updateCustomer(@PathVariable String id,@RequestBody UpdateCustomerRequest request) {
        CustomerResponse customer = customerService.updateCustomer(request);
        customer.setId(id);

        return ResponseEntity.ok(CommonResponse.<CustomerResponse>builder()
                        .statusCode(HttpStatus.OK.value())
                        .message("Customer updated successfully")
                        .data(customer)
                .build());
    }

    @DeleteMapping(path = "/{id}")
    public ResponseEntity<CommonResponse<String>> deleteCustomer(@PathVariable String id) {
        customerService.deleteCustomer(id);

        return ResponseEntity.ok(CommonResponse.<String>builder()
                .statusCode(HttpStatus.OK.value())
                .message("Customer deleted successfully")
                .build());
    }

    @GetMapping
    public ResponseEntity<List<CustomerResponse>> getAllCustomers() {
        List<CustomerResponse> customers = customerService.getAllCustomers();
        if (customers == null || customers.isEmpty()) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Customer null");
        }
        return ResponseEntity.status(HttpStatus.OK).body(customers);
    }

    @GetMapping("/{id}")
    public ResponseEntity<CommonResponse<CustomerResponse>> getCustomer(@PathVariable String id) {
        CustomerResponse response = customerService.getCustomerById(id);

        return ResponseEntity.ok(CommonResponse.<CustomerResponse>builder()
                .statusCode(HttpStatus.OK.value())
                .message("Customer found")
                .data(response)
                .build());
    }
}

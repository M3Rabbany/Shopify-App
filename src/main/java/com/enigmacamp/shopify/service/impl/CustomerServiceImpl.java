package com.enigmacamp.shopify.service.impl;

import com.enigmacamp.shopify.entity.Customer;
import com.enigmacamp.shopify.model.customer.CustomerResponse;
import com.enigmacamp.shopify.model.customer.UpdateCustomerRequest;
import com.enigmacamp.shopify.repository.CustomerRepository;
import com.enigmacamp.shopify.service.CustomerService;
import com.enigmacamp.shopify.utils.CustomerSpecification;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@Service
@RequiredArgsConstructor
public class CustomerServiceImpl implements CustomerService {
    private final ValidatorService validatorService;
    private final CustomerRepository customerRepository;

    @Override
    public List<Customer> searchCustomer(String query) {
        Specification<Customer> specification = CustomerSpecification.getSpecification(query);
        List<Customer> customers = customerRepository.findAll(specification);
        if (customers.isEmpty()) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Customer not found");
        }
        return customerRepository.findAll(specification);
    }

    @Override
    @Transactional
    public CustomerResponse createCustomer(Customer customer) {
        validatorService.validate(customer);

        Customer customerSaved = customerRepository.saveAndFlush(customer);
        return toCustomerResponse(customerSaved);
    }

    @Override
    @Transactional
    public CustomerResponse updateCustomer(UpdateCustomerRequest request) {
        validatorService.validate(request);

        Customer customer = customerRepository.findFirstById(request.getId())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Customer not found"));

        if (request.getId() == null) {
            throw new jakarta.validation.ValidationException("id is required");
        }

        customer.setName(request.getName());
        customer.setEmail(request.getEmail());
        customer.setPhone(request.getPhone());
        customer.setAddress(request.getAddress());
        customerRepository.save(customer);

        return toCustomerResponse(customer);
    }

    @Override
    public CustomerResponse getCustomerById(String id) {
        Customer customer = customerRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Customer not found"));

        return toCustomerResponse(customer);
    }

    @Override
    public List<CustomerResponse> getAllCustomers() {
        if (customerRepository.findAll().isEmpty()) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Customer null");
        }
        return customerRepository.findAll().stream()
                .map(this::toCustomerResponse)
                .toList();
    }

    @Override
    @Transactional
    public void deleteCustomer(String id) {
        Customer customer = customerRepository.findById(id).orElseThrow(
                () -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Customer not found")
        );
        customerRepository.delete(customer);
    }

    private CustomerResponse toCustomerResponse(Customer customer) {
        return CustomerResponse.builder()
                .id(customer.getId())
                .name(customer.getName())
                .email(customer.getEmail())
                .phone(customer.getPhone())
                .address(customer.getAddress())
                .createdAt(customer.getCreateAt())
                .image(customer.getImage())
                .build();
    }
}

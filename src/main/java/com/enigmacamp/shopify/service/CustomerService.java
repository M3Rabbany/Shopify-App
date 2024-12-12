package com.enigmacamp.shopify.service;

import com.enigmacamp.shopify.entity.Customer;
import com.enigmacamp.shopify.model.customer.CustomerRequest;
import com.enigmacamp.shopify.model.customer.CustomerResponse;
import com.enigmacamp.shopify.model.customer.UpdateCustomerRequest;

import java.util.List;

public interface CustomerService {
    List<Customer> searchCustomer(String query);
    CustomerResponse createCustomer(Customer customer);
    CustomerResponse updateCustomer(UpdateCustomerRequest request);
    List<CustomerResponse> getAllCustomers();
    CustomerResponse getCustomerById(String customerId);
    void deleteCustomer(String id);
}

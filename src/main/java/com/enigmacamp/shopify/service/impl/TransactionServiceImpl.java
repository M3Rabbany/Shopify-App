package com.enigmacamp.shopify.service.impl;

import com.enigmacamp.shopify.entity.*;
import com.enigmacamp.shopify.exception.ValidationException;
import com.enigmacamp.shopify.model.customer.CustomerResponse;
import com.enigmacamp.shopify.model.product.ProductRequest;
import com.enigmacamp.shopify.model.product.ProductResponse;
import com.enigmacamp.shopify.model.transaction.TransactionRequest;
import com.enigmacamp.shopify.model.transaction.TransactionResponse;
import com.enigmacamp.shopify.repository.*;
import com.enigmacamp.shopify.service.CustomerService;
import com.enigmacamp.shopify.service.ProductService;
import com.enigmacamp.shopify.service.TransactionService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.time.Instant;
import java.util.Date;
import java.util.List;
import java.util.concurrent.atomic.AtomicReference;

@Service
@RequiredArgsConstructor
public class TransactionServiceImpl implements TransactionService {
    private final ValidatorService validatorService;
    private final TransactionDetailRepository transactionDetailRepository;
    private final TransactionRepository transactionRepository;
    private final ProductService productService;
    private final CustomerService customerService;

    AtomicReference<Long> totalPayment = new AtomicReference<>(0L);

    @Override
    @Transactional(rollbackFor = Exception.class)
    public TransactionResponse create(TransactionRequest request) {
        validatorService.validate(request);

//        TODO: 1. Validate Id Customer
        CustomerResponse response = customerService.getCustomerById(request.getCustomerId());

//        TODO: 2. Prepare Data Customer
        Customer customer = Customer.builder()
                .id(response.getId())
                .name(response.getName())
                .email(response.getEmail())
                .phone(response.getPhone())
                .address(response.getAddress())
                .createAt(response.getCreatedAt())
                .build();

//        TODO: 3. Prepare Data Transaction
        Transaction transaction = Transaction.builder()
                .customer(customer)
                .transactionDate(Date.from(Instant.now()))
                .build();
//        TODO: 4. Create Data Transaction
        transaction = transactionRepository.save(transaction);



        // TODO: 5. Update stock, Create total payment, insert Transaction detail

//        TODO: Generate Transaction for Transaction Detail
        Transaction finalTransaction = transaction;
        List<TransactionDetail> transactionDetails = request.getTransactionDetailRequests().stream()
                .map(detail -> {

//                    TODO: Get Product
                    ProductResponse productResponse = productService.getProductById(detail.getProductId());
                    if (productResponse.getStock() < detail.getQuantity()) throw new ValidationException("Stock not enough",null);

//                    TODO: Update stock product
                    int stock = productResponse.getStock() - detail.getQuantity();
                    productService.updateStock(ProductRequest.builder()
                            .id(productResponse.getId())
                            .name(productResponse.getName())
                            .price(productResponse.getPrice())
                            .stock(stock)
                            .build());

//                    TODO: Prepare data Transaction Detail
                    Product product = Product.builder()
                            .id(productResponse.getId())
                            .name(productResponse.getName())
                            .price(productResponse.getPrice())
                            .stock(stock)
                            .createAt(productResponse.getCreated())
                            .build();

                    TransactionDetail transactionDetail = TransactionDetail.builder()
                            .transaction(finalTransaction)
                            .product(product)
                            .quantity(detail.getQuantity())
                            .totalPrice(product.getPrice() * detail.getQuantity())
                            .build();

//                    TODO: Total Payment
                    totalPayment.updateAndGet(value -> value + transactionDetail.getTotalPrice());

//                    TODO: Create Transaction Detail
                    transactionDetailRepository.save(transactionDetail);
                    return transactionDetail;
                }).toList();

//        TODO: Generate Transaction Detail for Transaction
        transaction.setTransactionDetails(transactionDetails);

        return TransactionResponse.builder()
                .id(transaction.getId())
                .customer(customer)
                .transactionDate(transaction.getTransactionDate())
                .transactionDetails(transaction.getTransactionDetails())
                .totalPayment(totalPayment.get())
                .build();
    }

    @Override
    public void delete(String id) {
//        TODO: Get Transaction id
        Transaction transaction = transactionRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND,"Transaction not found"));
//        TODO: Delete Transaction
        transactionRepository.delete(transaction);
    }

    @Override
    public TransactionResponse getById(String id) {
//        TODO: Get Transaction id
        Transaction transaction = transactionRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND,"Transaction not found"));
//        TODO: Show Response Data Transaction
        return toTransactionResponse(transaction);
    }

    @Override
    public List<TransactionResponse> getAll() {
//        TODO: Get All Transaction
        if (transactionRepository.findAll().isEmpty()) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND,"Transaction not found");
        }
//        TODO: Show All Data Transaction
        return transactionRepository.findAll().stream()
                .map(this::toTransactionResponse)
                .toList();
    }


//    private Payment createPayment(Transaction transaction) {
//        Long totalPayment = transaction.getTransactionDetails()
//                        .stream().mapToLong(TransactionDetail::getTotalPrice)
//                .sum();
//
//        Payment payment = Payment.builder()
//                .id(UUID.randomUUID().toString())
//                .totalPayments(totalPayment)
//                .transaction(transaction)
//                .build();
//
//        if (transaction.getPayment() == null) {
//            transaction.setPayment(payment);
//        }else {
//            throw new ResponseStatusException(HttpStatus.CONFLICT, "Payment already exists");
//        }
//        return payment;
//    }

//    TODO: Return Data Transaction
    public TransactionResponse toTransactionResponse(Transaction transaction) {
        return TransactionResponse.builder()
                .id(transaction.getId())
                .customer(transaction.getCustomer())
                .transactionDate(transaction.getTransactionDate())
                .transactionDetails(transaction.getTransactionDetails())
                .totalPayment(totalPayment.get())
                .build();
    }
}

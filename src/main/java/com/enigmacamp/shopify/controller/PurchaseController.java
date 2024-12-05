package com.enigmacamp.shopify.controller;

import com.enigmacamp.shopify.model.CommonResponse;
import com.enigmacamp.shopify.model.transaction.TransactionRequest;
import com.enigmacamp.shopify.model.transaction.TransactionResponse;
import com.enigmacamp.shopify.service.TransactionService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/transaction")
public class PurchaseController {
    private final TransactionService transactionService;

    @PostMapping()
    public ResponseEntity<CommonResponse<TransactionResponse>> createTransaction(@Valid @RequestBody TransactionRequest transactionRequest) {
        TransactionResponse transactionResponse = transactionService.create(transactionRequest);

        CommonResponse<TransactionResponse> response = CommonResponse.<TransactionResponse>builder()
                .statusCode(HttpStatus.CREATED.value())
                .message("Transaction created!")
                .data(transactionResponse)
                .build();

        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @GetMapping("/{id}")
    public ResponseEntity<CommonResponse<TransactionResponse>> getTransactionById(@PathVariable String id) {
        TransactionResponse response = transactionService.getById(id);
        return ResponseEntity.ok(CommonResponse.<TransactionResponse>builder()
                .statusCode(HttpStatus.OK.value())
                .message("Transaction found!")
                .data(response)
                .build());
    }

    @GetMapping
    public ResponseEntity<CommonResponse<List<TransactionResponse>>> getTransactions() {
        List<TransactionResponse> transactions = transactionService.getAll();
        return ResponseEntity.ok(CommonResponse.<List<TransactionResponse>>builder()
                .statusCode(HttpStatus.OK.value())
                .message("Data Transaction found!")
                .data(transactions)
                .build());
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<CommonResponse<String>> deleteTransaction(@PathVariable String id) {
        transactionService.delete(id);

        return ResponseEntity.ok(CommonResponse.<String>builder()
                .message("Transaction deleted!")
                .statusCode(HttpStatus.NO_CONTENT.value())
                .build());
    }
}

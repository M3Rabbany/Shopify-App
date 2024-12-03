package com.enigmacamp.shopify.controller;

import com.enigmacamp.shopify.model.CommonResponse;
import com.enigmacamp.shopify.model.product.ProductRequest;
import com.enigmacamp.shopify.model.product.ProductResponse;
import com.enigmacamp.shopify.service.impl.ProductServiceImpl;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/product")
@RequiredArgsConstructor
public class ProductController {
    private final ProductServiceImpl productService;

    @PostMapping
    public ResponseEntity<CommonResponse<ProductResponse>> create(@RequestBody ProductRequest payload) {

        // CALL LOGIC SERVICE
        ProductResponse productResponse = productService.create(payload);

        CommonResponse<ProductResponse> response = CommonResponse.<ProductResponse>builder()
                .statusCode(HttpStatus.CREATED.value())
                .message("New product added!")
                .data(productResponse)
                .build();

        return ResponseEntity
                .status(HttpStatus.CREATED.value())
                .body(response);
    }

    @GetMapping
    public ResponseEntity<List<ProductResponse>> getAllProduct(@RequestParam(required = false) String name)  {
        List<ProductResponse> allProducts = productService.getAllProducts(name);
        return ResponseEntity.status(HttpStatus.OK.value()).body(allProducts);
    }

    @Operation(summary = "Get product by ID")
    @ApiResponses({
            @ApiResponse(
                    responseCode = "200",
                    description = "Product found"
            ),
            @ApiResponse(
                    responseCode = "404",
                    description = "Product not found"
            )
    })
    @GetMapping("/{id}")
    public ResponseEntity<CommonResponse<ProductResponse>> getProductById(@PathVariable String id) {
        ProductResponse productResponse = productService.getProductById(id);
        return ResponseEntity.ok(CommonResponse.<ProductResponse>builder()
                        .message("Product found!")
                        .statusCode(HttpStatus.OK.value())
                        .data(productResponse)
                .build());
    }

    @PutMapping("/{id}")
    public ResponseEntity<CommonResponse<ProductResponse>> update(@PathVariable String id, @RequestBody ProductRequest payload) {

        ProductResponse update = productService.update(payload);
        update.setId(id);

        return ResponseEntity.ok(CommonResponse.<ProductResponse>builder()
                        .statusCode(HttpStatus.OK.value())
                        .message("Product updated!")
                        .data(update)
                .build());
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<CommonResponse<String>> deleteProduct(@PathVariable String id) {
        productService.delete(id);

        return ResponseEntity.ok(CommonResponse.<String>builder()
                        .message("Product deleted!")
                        .data(null)
                        .statusCode(HttpStatus.OK.value())
                .build());
    }
}

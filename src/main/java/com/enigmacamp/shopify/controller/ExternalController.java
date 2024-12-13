package com.enigmacamp.shopify.controller;

import com.enigmacamp.shopify.model.CommonResponse;
import com.enigmacamp.shopify.model.RequestData;
import com.enigmacamp.shopify.model.ResponseData;
import com.enigmacamp.shopify.service.ExternalService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/posts")
@RequiredArgsConstructor
public class ExternalController {
    private final ExternalService externalService;

    @GetMapping("/{id}")
    public ResponseEntity<ResponseData> getData(@PathVariable String id) {
        return ResponseEntity.ok(externalService.getData(id));
    }

    @GetMapping
    public ResponseEntity<List<ResponseData>> getAll() {
        return ResponseEntity.ok(externalService.getAll());
    }

    @PostMapping
    public ResponseEntity<ResponseData> create(@RequestBody RequestData request) {
        return ResponseEntity.status(HttpStatus.CREATED).body(externalService.create(request));
    }

    @PutMapping("/{id}")
    public ResponseEntity<ResponseData> update(@PathVariable String id, @RequestBody RequestData request) {
        return ResponseEntity.ok(externalService.update(id, request));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<CommonResponse<Void>> delete(@PathVariable String id) {
        externalService.delete(id);
        return ResponseEntity.ok(CommonResponse.<Void>builder()
                .message("Deleted!")
                .statusCode(HttpStatus.OK.value())
                .data(null)
                .build());
    }
}

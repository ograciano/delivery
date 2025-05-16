package com.delivery.catalog.controller;

import com.delivery.catalog.model.Product;
import com.delivery.catalog.model.ProductSearch;
import com.delivery.catalog.service.ProductElasticsearchService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/products/search")
@RequiredArgsConstructor
public class ProductSearchController {

    private final ProductElasticsearchService elasticsearchService;

    @GetMapping
    public Iterable<ProductSearch> getAllIndexedProducts() {
        return elasticsearchService.findAll();
    }

    @GetMapping("/{id}")
    public ResponseEntity<ProductSearch> getProductById(@PathVariable Long id) {
        return elasticsearchService.findById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping("/name/{name}")
    public Iterable<ProductSearch> searchProductsByName(@PathVariable String name) {
        return elasticsearchService.searchByName(name);
    }
}


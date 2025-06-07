package com.delivery.catalog.controller;

import com.delivery.catalog.model.Product;
import com.delivery.catalog.service.ProductImageService;
import com.delivery.catalog.service.ProductService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.http.codec.multipart.FilePart;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.List;

@RestController
@RequestMapping("/api/products")
@RequiredArgsConstructor
public class ProductController {

    private final ProductService productService;
    private final ProductImageService productImageService;

    @GetMapping
    public Flux<Product> getAllProducts() {
        return productService.getAllProducts();
    }

    @PostMapping
    public Mono<Product> createProduct(@RequestBody Product product) {
        return productService.createProduct(product);
    }

    @PostMapping("/update-stock")
    public Mono<Void> updateStockAfterOrder(@RequestBody List<Product> productsOrdered) {
        return productService.updateStock(productsOrdered);
    }

    @PostMapping(value = "/{id}/files", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public Mono<ResponseEntity<Product>> uploadProductImages(
            @PathVariable Long id,
            @RequestPart("principalImage") Mono<FilePart> principalImage,
            @RequestPart(value = "galleryImages", required = false) Flux<FilePart> galleryImages) {

        return productImageService.uploadImages(id, principalImage, galleryImages)
                .map(ResponseEntity::ok);
    }
}



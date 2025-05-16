package com.delivery.catalog.service;

import com.delivery.catalog.mappers.Mappers;
import com.delivery.catalog.model.Product;
import com.delivery.catalog.model.ProductSearch;
import com.delivery.catalog.repository.ProductRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.List;

@Service
@Slf4j
@RequiredArgsConstructor
public class ProductService {

    private final ProductRepository productRepository;
    private final ProductElasticsearchService productElasticsearchService;

    public Flux<Product> getAllProducts() {
        List<Product> products = productRepository.findAll();
        return Flux.fromIterable(products);
    }

    public Mono<Product> createProduct(Product product) {

        Product saved = productRepository.save(product);
        ProductSearch productSearch = Mappers.map(product, ProductSearch.class);
        productElasticsearchService.save(productSearch);
        return Mono.just(saved);
    }

    public Mono<Void> updateStock(List<Product> productsOrdered) {
        productsOrdered.forEach(productOrdered -> {
            log.info("Actualizando stock para el producto: {}", productOrdered.getName());
            log.info("Stock solicitado: {}", productOrdered.getStockKg());
            log.info("ID del producto: {}", productOrdered.getId());
            log.info("ID del comerciante: {}", productOrdered.getMerchantId());
            log.info("Nombre del comerciante: {}", productOrdered.getMerchantName());
            log.info("Precio por kg: {}", productOrdered.getPricePerKg());
            Product productInDb = productRepository.findById(productOrdered.getId())
                    .orElseThrow(() -> new RuntimeException("Producto no encontrado con id: " + productOrdered.getId()));

            double newStock = productOrdered.getStockKg();
            if (newStock < 0) {
                throw new RuntimeException("Stock insuficiente para producto: " + productInDb.getName());
            }

            log.info("Stock actualizado de {} a {}", productInDb.getStockKg(), newStock);

            productInDb.setStockKg(newStock);
            productRepository.save(productInDb);
            ProductSearch productSearch = Mappers.map(productInDb, ProductSearch.class);
            productElasticsearchService.save(productSearch);
        });
        return Mono.empty();
    }

    public Mono<Product> findById(Long id) {
        return Mono.just(productRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Product not found with id: " + id)));
    }
}

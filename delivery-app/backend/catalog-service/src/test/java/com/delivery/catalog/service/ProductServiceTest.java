package com.delivery.catalog.service;

import com.delivery.catalog.model.Product;
import com.delivery.catalog.model.ProductSearch;
import com.delivery.catalog.repository.ProductRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Collections;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ProductServiceTest {

    @Mock
    private ProductRepository productRepository;
    @Mock
    private ProductElasticsearchService productElasticsearchService;

    private ProductService productService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        productService = new ProductService(productRepository, productElasticsearchService);
    }

    @Test
    void updateStockUpdatesProductWhenStockValid() {
        Product existing = Product.builder().id(1L).name("Apple").stockKg(10.0).build();
        Product ordered = Product.builder().id(1L).stockKg(5.0).build();

        when(productRepository.findById(1L)).thenReturn(Optional.of(existing));
        when(productRepository.save(any(Product.class))).thenAnswer(invocation -> invocation.getArgument(0));

        productService.updateStock(Collections.singletonList(ordered)).block();

        assertEquals(5.0, existing.getStockKg());
        verify(productRepository).save(existing);
        verify(productElasticsearchService).save(any(ProductSearch.class));
    }

    @Test
    void updateStockFailsWhenNegative() {
        Product existing = Product.builder().id(1L).name("Apple").stockKg(10.0).build();
        Product ordered = Product.builder().id(1L).stockKg(-1.0).build();

        when(productRepository.findById(1L)).thenReturn(Optional.of(existing));

        RuntimeException ex = assertThrows(RuntimeException.class, () ->
                productService.updateStock(Collections.singletonList(ordered)).block());

        assertTrue(ex.getMessage().contains("Stock insuficiente"));
        verify(productRepository, never()).save(any());
    }
}

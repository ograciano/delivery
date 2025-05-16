package com.delivery.catalog.controller;

import com.delivery.catalog.model.Category;
import com.delivery.catalog.service.CategoryService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping("/api/categories")
@RequiredArgsConstructor
public class CategoryController {

    private final CategoryService categoryService;

    @GetMapping
    public Flux<Category> getAllCategories() {
        return categoryService.getAllCategories();
    }

    @PostMapping
    public Mono<Category> createCategory(@RequestBody Category category) {
        return categoryService.createCategory(category);
    }
}


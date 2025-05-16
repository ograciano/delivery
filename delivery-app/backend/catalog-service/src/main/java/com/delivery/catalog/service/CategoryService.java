package com.delivery.catalog.service;

import com.delivery.catalog.model.Category;
import com.delivery.catalog.repository.CategoryRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.List;

@Service
@RequiredArgsConstructor
public class CategoryService {

    private final CategoryRepository categoryRepository;

    public Flux<Category> getAllCategories() {
        List<Category> categories = categoryRepository.findAll();
        return Flux.fromIterable(categories);
    }

    public Mono<Category> createCategory(Category category) {
        Category saved = categoryRepository.save(category);
        return Mono.just(saved);
    }
}

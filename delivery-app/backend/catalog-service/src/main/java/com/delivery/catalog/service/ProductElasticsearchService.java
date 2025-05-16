package com.delivery.catalog.service;

import com.delivery.catalog.model.Product;
import com.delivery.catalog.elasticsearch.repository.ProductElasticsearchRepository;
import com.delivery.catalog.model.ProductSearch;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class ProductElasticsearchService {

    private final ProductElasticsearchRepository repository;

    public ProductSearch save(ProductSearch product) {
        return repository.save(product);
    }

    public List<ProductSearch> saveAll(List<ProductSearch> products) {
        return (List<ProductSearch>) repository.saveAll(products);
    }

    public Optional<ProductSearch> findById(Long id) {
        return repository.findById(id);
    }

    public Iterable<ProductSearch> findAll() {
        return repository.findAll();
    }

    public void deleteById(Long id) {
        repository.deleteById(id);
    }

    public void deleteAll() {
        repository.deleteAll();
    }

    public List<ProductSearch> searchByName(String name) {
        return repository.findByNameContainingIgnoreCase(name);
    }
}

package com.delivery.catalog.elasticsearch.repository;

import com.delivery.catalog.model.Product;
import com.delivery.catalog.model.ProductSearch;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ProductElasticsearchRepository extends ElasticsearchRepository<ProductSearch, Long> {
    List<ProductSearch> findByNameContainingIgnoreCase(String name);
}

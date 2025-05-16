package com.delivery.catalog.repository;

import com.delivery.catalog.model.Product;
import org.springframework.data.jpa.repository.JpaRepository;
import reactor.core.publisher.Mono;

import java.util.Optional;

public interface ProductRepository extends JpaRepository<Product, Long> {
}

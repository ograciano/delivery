package com.delivery.catalog.repository;

import com.delivery.catalog.model.Category;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CategoryRepository extends JpaRepository<Category, Long> {
}
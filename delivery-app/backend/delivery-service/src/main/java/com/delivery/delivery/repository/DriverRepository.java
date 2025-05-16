package com.delivery.delivery.repository;

import com.delivery.delivery.model.Driver;
import org.springframework.data.jpa.repository.JpaRepository;

public interface DriverRepository extends JpaRepository<Driver, Long> {
}
package com.delivery.delivery.repository;

import com.delivery.delivery.model.Assignment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import reactor.core.publisher.Mono;

import java.util.Optional;

public interface AssignmentRepository extends JpaRepository<Assignment, Long> {
    Optional<Assignment> findByOrderId(Long orderId);
    Assignment findByDriverId(Long driverId);
    Assignment findByStatus(String status);
}
package com.delivery.delivery.service;

import com.delivery.delivery.client.OrderClient;
import com.delivery.delivery.model.Assignment;
import com.delivery.delivery.model.Driver;
import com.delivery.delivery.model.Order;
import com.delivery.delivery.repository.AssignmentRepository;
import com.delivery.delivery.repository.DriverRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.List;

@Service
@RequiredArgsConstructor
public class AssignmentService {

    private final AssignmentRepository assignmentRepository;
    private final DriverRepository driverRepository;
    private final OrderClient orderClient;

    public Flux<Assignment> getAllAssignments() {
        List<Assignment> assignments = assignmentRepository.findAll();
        return Flux.fromIterable(assignments);
    }

    public Mono<Assignment> assignOrder(Long orderId, Long driverId) {
        return Mono.fromCallable(() -> {
            Driver driver = driverRepository.findById(driverId)
                    .orElseThrow(() -> new RuntimeException("Driver not found"));

            if (!Boolean.TRUE.equals(driver.getAvailable())) {
                throw new RuntimeException("Driver is not available");
            }

            driver.setAvailable(false);
            driverRepository.save(driver);

            Assignment assignment = Assignment.builder()
                    .orderId(orderId)
                    .driverId(driverId)
                    .status("assigned")
                    .build();

            Assignment savedAssignment = assignmentRepository.save(assignment);
            return savedAssignment;
        }).flatMap(assignment ->
                orderClient.updateOrderStatus(orderId, "assigned").thenReturn(assignment)
        );
    }

    public Mono<Assignment> updateStatus(Long assignmentId, String status) {
        return Mono.fromCallable(() -> {
            Assignment assignment = assignmentRepository.findById(assignmentId)
                    .orElseThrow(() -> new RuntimeException("Assignment not found"));

            assignment.setStatus(status);
            Assignment updatedAssignment = assignmentRepository.save(assignment);

            return updatedAssignment;
        }).flatMap(updatedAssignment -> {
            if ("delivered".equalsIgnoreCase(updatedAssignment.getStatus())) {
                return Mono.fromCallable(() -> {
                    Driver driver = driverRepository.findById(updatedAssignment.getDriverId())
                            .orElseThrow(() -> new RuntimeException("Driver not found"));
                    driver.setAvailable(true);
                    driverRepository.save(driver);
                    return updatedAssignment;
                }).flatMap(u -> orderClient.updateOrderStatus(u.getOrderId(), "delivered").thenReturn(u));
            } else {
                return Mono.just(updatedAssignment);
            }
        });
    }

    public Flux<Order> getAvailableOrders() {
        return orderClient.getPendingOrders();
    }
}

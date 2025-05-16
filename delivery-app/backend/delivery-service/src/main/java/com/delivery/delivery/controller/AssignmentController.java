package com.delivery.delivery.controller;

import com.delivery.delivery.model.Assignment;
import com.delivery.delivery.model.Order;
import com.delivery.delivery.service.AssignmentService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping("/api/assignments")
@RequiredArgsConstructor
public class AssignmentController {

    private final AssignmentService assignmentService;

    @GetMapping
    public Flux<Assignment> getAllAssignments() {
        return assignmentService.getAllAssignments();
    }

    @PostMapping
    public Mono<Assignment> assignOrder(@RequestParam Long orderId, @RequestParam Long driverId) {
        return assignmentService.assignOrder(orderId, driverId);
    }

    @PatchMapping("/{id}")
    public Mono<Assignment> updateStatus(@PathVariable Long id, @RequestParam String status) {
        return assignmentService.updateStatus(id, status);
    }

    @GetMapping("/available-orders")
    public Flux<Order> getAvailableOrders() {
        return assignmentService.getAvailableOrders();
    }
}


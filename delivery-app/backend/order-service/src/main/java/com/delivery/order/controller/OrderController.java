package com.delivery.order.controller;

import com.delivery.order.model.MerchantFinancialSummary;
import com.delivery.order.model.MerchantSalesSummary;
import com.delivery.order.model.Order;
import com.delivery.order.service.OrderService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping("/api/orders")
@RequiredArgsConstructor
public class OrderController {

    private final OrderService orderService;

    @GetMapping
    public Flux<Order> getAllOrders() {
        return orderService.getAllOrders();
    }

    @GetMapping("/pending")
    public Flux<Order> getPendingOrders() {
        return orderService.getPendingOrders();
    }

    @PostMapping
    public Mono<Order> createOrder(@RequestBody Order order) {
        return orderService.createOrder(order);
    }

    @PatchMapping("/{id}")
    public Mono<Order> updateOrderStatus(@PathVariable Long id, @RequestParam String status) {
        return orderService.updateOrderStatus(id, status);
    }

    @GetMapping("/merchant/{merchantId}/sales")
    public Flux<Order> getSalesByMerchant(@PathVariable Long merchantId) {
        return orderService.getSalesByMerchant(merchantId);
    }

    @GetMapping("/merchant/{merchantId}/sales-summary")
    public Mono<MerchantSalesSummary> getMerchantSalesSummary(@PathVariable Long merchantId) {
        return orderService.getMerchantSalesSummary(merchantId);
    }

    @GetMapping("/merchant/{merchantId}/pending-payments")
    public Flux<Order> getPendingPayments(@PathVariable Long merchantId) {
        return orderService.getPendingPayments(merchantId);
    }

    @PatchMapping("/mark-paid/{orderId}")
    public Mono<Order> markOrderAsPaid(@PathVariable Long orderId) {
        return orderService.markOrderAsPaid(orderId);
    }

    @GetMapping("/merchant/{merchantId}/financial-summary")
    public Mono<MerchantFinancialSummary> getMerchantFinancialSummary(
            @PathVariable Long merchantId,
            @RequestParam String startDate,
            @RequestParam String endDate
    ) {
        return orderService.getMerchantFinancialSummary(merchantId, startDate, endDate);
    }
}


package com.delivery.order.service;

import com.delivery.order.client.CatalogClient;
import com.delivery.order.event.OrderEventPublisher;
import com.delivery.order.model.MerchantFinancialSummary;
import com.delivery.order.model.MerchantSalesSummary;
import com.delivery.order.model.Order;
import com.delivery.order.model.Product;
import com.delivery.order.repository.OrderRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.amqp.core.DirectExchange;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

@Service
@RequiredArgsConstructor
public class OrderService {

    private final OrderRepository orderRepository;
    private final CatalogClient catalogClient;
    private final OrderEventPublisher orderEventPublisher;

    public Flux<Order> getAllOrders() {
        return Flux.defer(() -> Flux.fromIterable(orderRepository.findAll()));
    }

    public Flux<Order> getPendingOrders() {
        return Flux.defer(() -> Flux.fromIterable(orderRepository.findByStatus("pending")));
    }

    public Mono<Order> createOrder(Order order) {
        return catalogClient.getAllProducts()
                .collectList()
                .flatMap(availableProducts -> {
                    for (Product p : order.getProducts()) {
                        Product catalogProduct = availableProducts.stream()
                                .filter(product -> product.getId().equals(p.getId()))
                                .findFirst()
                                .orElseThrow(() -> new IllegalArgumentException("Producto no encontrado"));

                        if (catalogProduct.getStockKg() < p.getQuantityKg()) {
                            throw new IllegalArgumentException("Stock insuficiente para producto: " + catalogProduct.getName());
                        }

                        p.setName(catalogProduct.getName());
                        p.setPricePerKg(catalogProduct.getPricePerKg());
                        p.setMerchantId(catalogProduct.getMerchantId());
                        p.setMerchantName(catalogProduct.getMerchantName());
                        p.setTotalPrice(catalogProduct.getPricePerKg() * p.getQuantityKg());
                        p.setStockKg(catalogProduct.getStockKg() - p.getQuantityKg());
                    }

                    order.setTotalAmount(order.getProducts().stream()
                            .mapToDouble(Product::getTotalPrice)
                            .sum());
                    order.setStatus("pending");
                    order.setCreatedDate(LocalDateTime.now());

                    Order savedOrder = orderRepository.save(order);
                    orderEventPublisher.publishOrderCreated(savedOrder);

                    return catalogClient.updateStockAfterOrder(order.getProducts())
                            .thenReturn(savedOrder);
                });
    }

    public Flux<Order> getSalesByMerchant(Long merchantId) {
        return Flux.defer(() -> Flux.fromIterable(orderRepository.findAll()))
                .filter(order -> order.getProducts().stream()
                        .anyMatch(product -> merchantId.equals(product.getMerchantId())));
    }

    public Mono<MerchantSalesSummary> getMerchantSalesSummary(Long merchantId) {
        return Flux.defer(() -> Flux.fromIterable(orderRepository.findAll()))
                .filter(order -> "delivered".equalsIgnoreCase(order.getStatus()))
                .collectList()
                .map(deliveredOrders -> {
                    int totalProductsSold = 0;
                    double totalRevenue = 0.0;
                    String merchantName = null;
                    int totalOrders = 0;

                    for (Order order : deliveredOrders) {
                        boolean merchantInOrder = false;

                        for (Product product : order.getProducts()) {
                            if (merchantId.equals(product.getMerchantId())) {
                                totalProductsSold++;
                                totalRevenue += product.getTotalPrice();
                                merchantName = product.getMerchantName();
                                merchantInOrder = true;
                            }
                        }

                        if (merchantInOrder) {
                            totalOrders++;
                        }
                    }

                    return MerchantSalesSummary.builder()
                            .merchantId(merchantId)
                            .merchantName(merchantName)
                            .totalRevenue(totalRevenue)
                            .totalProductsSold(totalProductsSold)
                            .totalOrders(totalOrders)
                            .build();
                });
    }

    public Flux<Order> getPendingPayments(Long merchantId) {
        return Flux.defer(() -> Flux.fromIterable(orderRepository.findAll()))
                .filter(order -> "delivered".equalsIgnoreCase(order.getStatus()))
                .filter(order -> Boolean.FALSE.equals(order.getPaidToMerchant()))
                .filter(order -> order.getProducts().stream()
                        .anyMatch(product -> merchantId.equals(product.getMerchantId())));
    }

    public Mono<Order> markOrderAsPaid(Long orderId) {
        return Mono.defer(() -> {
            Order order = orderRepository.findById(orderId)
                    .orElseThrow(() -> new RuntimeException("Pedido no encontrado"));

            if (!"delivered".equalsIgnoreCase(order.getStatus())) {
                throw new RuntimeException("Solo puedes pagar pedidos entregados");
            }

            if (Boolean.TRUE.equals(order.getPaidToMerchant())) {
                throw new RuntimeException("Este pedido ya fue pagado");
            }

            order.setPaidToMerchant(true);
            order.setPaymentDate(LocalDateTime.now());

            Order updatedOrder = orderRepository.save(order);
            orderEventPublisher.publishOrderPaid(updatedOrder);

            return Mono.just(updatedOrder);
        });
    }

    public Mono<MerchantFinancialSummary> getMerchantFinancialSummary(Long merchantId, String startDateStr, String endDateStr) {
        DateTimeFormatter formatter = DateTimeFormatter.ISO_DATE_TIME;
        LocalDateTime startDate = LocalDateTime.parse(startDateStr, formatter);
        LocalDateTime endDate = LocalDateTime.parse(endDateStr, formatter);

        return Flux.defer(() -> Flux.fromIterable(orderRepository.findAll()))
                .filter(order -> "delivered".equalsIgnoreCase(order.getStatus()))
                .filter(order -> order.getProducts().stream()
                        .anyMatch(product -> merchantId.equals(product.getMerchantId())))
                .filter(order -> {
                    if (Boolean.TRUE.equals(order.getPaidToMerchant())) {
                        return order.getPaymentDate() != null &&
                                !order.getPaymentDate().isBefore(startDate) &&
                                !order.getPaymentDate().isAfter(endDate);
                    } else {
                        return order.getCreatedDate() != null &&
                                !order.getCreatedDate().isBefore(startDate) &&
                                !order.getCreatedDate().isAfter(endDate);
                    }
                })
                .collectList()
                .map(filteredOrders -> {
                    double totalRevenue = 0.0;
                    double totalPaid = 0.0;
                    double totalPending = 0.0;
                    String merchantName = null;
                    int totalOrders = 0;

                    for (Order order : filteredOrders) {
                        for (Product product : order.getProducts()) {
                            if (merchantId.equals(product.getMerchantId())) {
                                totalRevenue += product.getTotalPrice();
                                if (Boolean.TRUE.equals(order.getPaidToMerchant())) {
                                    totalPaid += product.getTotalPrice();
                                } else {
                                    totalPending += product.getTotalPrice();
                                }
                                merchantName = product.getMerchantName();
                            }
                        }
                        totalOrders++;
                    }

                    return MerchantFinancialSummary.builder()
                            .merchantId(merchantId)
                            .merchantName(merchantName)
                            .totalRevenue(totalRevenue)
                            .totalPaid(totalPaid)
                            .totalPending(totalPending)
                            .totalOrders(totalOrders)
                            .build();
                });
    }

    public Mono<Order> updateOrderStatus(Long orderId, String status) {
        return Mono.fromCallable(() -> {
            var order = orderRepository.findById(orderId)
                    .orElseThrow(() -> new RuntimeException("Order not found"));
            order.setStatus(status);
            Order savedOrder = orderRepository.save(order);
            sendMessageStatus(status, savedOrder);
            return savedOrder;
        });
    }

    private void sendMessageStatus(String status, Order order) {
        switch (status) {
            case "pending":
                break;
            case "delivered":
                orderEventPublisher.publishOrderDelivered(order);
                break;
            case "cancelled":
                orderEventPublisher.publishOrderCancelled(order);
                break;
            default:
                orderEventPublisher.publishOrderUpdated(order);

        }
    }
}

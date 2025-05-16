package com.delivery.delivery.client;

import com.delivery.delivery.model.Order;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Component
@RequiredArgsConstructor
public class OrderClient {

    @Value("${order.service-url}")
    private String orderServiceUrl;

    private WebClient webClient;

    @PostConstruct
    public void init() {
        this.webClient = WebClient.builder()
                .baseUrl(orderServiceUrl)
                .build();
    }

    public Flux<Order> getPendingOrders() {
        return webClient.get()
                .uri("/pending")
                .retrieve()
                .bodyToFlux(Order.class);
    }

    public Mono<Void> updateOrderStatus(Long orderId, String status) {
        return webClient.patch()
                .uri("/{id}?status={status}", orderId, status)
                .retrieve()
                .bodyToMono(Void.class);
    }
}


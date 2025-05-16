package com.delivery.order.client;

import com.delivery.order.model.Product;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.List;

@Component
@RequiredArgsConstructor
public class CatalogClient {

    @Value("${catalog.service-url}")
    private String catalogServiceUrl;

    private WebClient webClient;

    @PostConstruct
    public void init() {
        this.webClient = WebClient.builder()
                .baseUrl(catalogServiceUrl)
                .build();
    }

    public Flux<Product> getAllProducts() {
        return webClient.get()
                .uri("")
                .retrieve()
                .bodyToFlux(Product.class);
    }

    public Mono<Void> updateStockAfterOrder(List<Product> products) {
        return webClient.post()
                .uri("/update-stock")
                .bodyValue(products)
                .retrieve()
                .bodyToMono(Void.class);
    }
}

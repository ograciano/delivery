package com.delivery.gateway.config;

import com.delivery.gateway.filter.AuthenticationFilter;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.gateway.filter.GatewayFilter;
import org.springframework.cloud.gateway.route.RouteLocator;
import org.springframework.cloud.gateway.route.builder.RouteLocatorBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@RequiredArgsConstructor
public class GatewayConfig {

    @Autowired
    private final AuthenticationFilter authenticationFilter;

    @Bean
    public RouteLocator routes(RouteLocatorBuilder builder) {
        return builder.routes()
                .route("auth-service", r -> r.path("/api/auth/**")
                        .uri("http://auth-service:8084"))
                .route("catalog-service", r -> r.path("/api/products/**", "/api/categories/**")
                        .filters(f -> f.filter(authenticationFilter))
                        .uri("http://catalog-service:8081"))
                .route("order-service", r -> r.path("/api/orders/**")
                        .filters(f -> f.filter(authenticationFilter))
                        .uri("http://order-service:8082"))
                .route("delivery-service", r -> r.path("/api/drivers/**", "/api/assignments/**")
                        .filters(f -> f.filter(authenticationFilter))
                        .uri("http://delivery-service:8083"))
                .route("file-service", r -> r.path("/api/files/**")
                        .filters(f -> f.filter(authenticationFilter))
                        .uri("http://file-service:8086"))
                // OpenAPI documentation endpoints for each microservice
                .route("auth-service-docs", r -> r.path("/api/docs/auth")
                        .filters(f -> f.rewritePath("/api/docs/auth", "/v3/api-docs"))
                        .uri("http://auth-service:8084"))
                .route("catalog-service-docs", r -> r.path("/api/docs/catalog")
                        .filters(f -> f.rewritePath("/api/docs/catalog", "/v3/api-docs"))
                        .uri("http://catalog-service:8081"))
                .route("order-service-docs", r -> r.path("/api/docs/order")
                        .filters(f -> f.rewritePath("/api/docs/order", "/v3/api-docs"))
                        .uri("http://order-service:8082"))
                .route("delivery-service-docs", r -> r.path("/api/docs/delivery")
                        .filters(f -> f.rewritePath("/api/docs/delivery", "/v3/api-docs"))
                        .uri("http://delivery-service:8083"))
                .route("file-service-docs", r -> r.path("/api/docs/file")
                        .filters(f -> f.rewritePath("/api/docs/file", "/v3/api-docs"))
                        .uri("http://file-service:8086"))
                .build();
    }
}


package com.delivery.gateway.exception;

import org.springframework.core.annotation.Order;
import org.springframework.core.Ordered;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import org.springframework.web.server.WebExceptionHandler;
import reactor.core.publisher.Mono;

@Component
@Order(Ordered.HIGHEST_PRECEDENCE)
public class GlobalExceptionHandler implements WebExceptionHandler {

    @Override
    public Mono<Void> handle(ServerWebExchange exchange, Throwable ex) {
        exchange.getResponse().setStatusCode(HttpStatus.INTERNAL_SERVER_ERROR);
        exchange.getResponse().getHeaders().setContentType(MediaType.APPLICATION_JSON);

        String errorMessage = "{\"error\": \"Internal Server Error\"}";

        byte[] bytes = errorMessage.getBytes();
        return exchange.getResponse().writeWith(Mono.just(exchange.getResponse().bufferFactory().wrap(bytes)));
    }
}

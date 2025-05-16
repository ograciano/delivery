package com.delivery.gateway.filter;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.gateway.filter.GatewayFilter;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

import javax.crypto.SecretKey;

@Component
@Slf4j
public class AuthenticationFilter implements GatewayFilter {

    @Value("${jwt.secret}")
    private String jwtSecret;

    @Override
    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {
        log.info("AuthenticationFilter: Verifying JWT token");

        String authHeader = exchange.getRequest().getHeaders().getFirst(HttpHeaders.AUTHORIZATION);
        log.info("Authorization Header: {}", authHeader);

        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            log.warn("No Bearer token found, proceeding without authentication");
            return chain.filter(exchange);
        }

        String token = authHeader.substring(7);

        try {
            SecretKey key = Keys.hmacShaKeyFor(jwtSecret.getBytes());

            Claims claims = Jwts.parserBuilder()
                    .setSigningKey(key)
                    .build()
                    .parseClaimsJws(token)
                    .getBody();

            log.info("JWT valid. Claims: {}", claims);

            // Si quieres pasar claims como headers (opcional)
            ServerWebExchange mutatedExchange = exchange.mutate()
                    .request(r -> r.headers(h -> {
                        h.add("X-User-Id", claims.getSubject());
                    }))
                    .build();

            return chain.filter(mutatedExchange);

        } catch (Exception e) {
            log.error("Invalid JWT token: {}", e.getMessage());
            exchange.getResponse().setStatusCode(HttpStatus.UNAUTHORIZED);
            return exchange.getResponse().setComplete();
        }
    }
}

package com.delivery.auth.security;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.ReactiveSecurityContextHolder;
import org.springframework.security.core.context.SecurityContextImpl;
import org.springframework.security.web.server.authentication.ServerAuthenticationConverter;
import org.springframework.security.web.server.authentication.ServerAuthenticationSuccessHandler;
import org.springframework.security.web.server.authentication.ServerAuthenticationFailureHandler;
import org.springframework.security.web.server.context.WebSessionServerSecurityContextRepository;
import org.springframework.security.web.server.util.matcher.ServerWebExchangeMatchers;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;
import org.springframework.web.server.WebFilter;
import org.springframework.security.core.Authentication;
import org.springframework.security.authentication.ReactiveAuthenticationManager;
import org.springframework.security.web.server.authentication.AuthenticationWebFilter;
import org.springframework.security.web.server.authentication.ServerAuthenticationConverter;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

import java.util.Collections;
import java.util.List;

@Component
@RequiredArgsConstructor
public class JwtAuthenticationFilter implements WebFilter {

    private final JwtProvider jwtProvider;

    @Override
    public Mono<Void> filter(ServerWebExchange exchange, org.springframework.web.server.WebFilterChain chain) {
        String authHeader = exchange.getRequest()
                .getHeaders()
                .getFirst(HttpHeaders.AUTHORIZATION);

        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            return chain.filter(exchange);
        }

        String token = authHeader.substring(7);

        if (!jwtProvider.validateToken(token)) {
            return chain.filter(exchange);
        }

        String username = jwtProvider.getUsernameFromToken(token);
        List<String> roles = jwtProvider.getRolesFromToken(token);

        List<SimpleGrantedAuthority> authorities = roles.stream()
                .map(SimpleGrantedAuthority::new)
                .toList();

        UserDetails userDetails = User.builder()
                .username(username)
                .password("") // no password en este contexto
                .authorities(authorities)
                .build();

        UsernamePasswordAuthenticationToken authentication =
                new UsernamePasswordAuthenticationToken(userDetails, null, authorities);

        return chain.filter(exchange)
                .contextWrite(ReactiveSecurityContextHolder.withAuthentication(authentication));
    }
}

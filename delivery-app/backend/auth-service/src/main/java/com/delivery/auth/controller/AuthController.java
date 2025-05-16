package com.delivery.auth.controller;

import com.delivery.auth.model.User;
import com.delivery.auth.service.AuthService;
import lombok.Builder;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Mono;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthController {

    @Builder
    record AuthRequest (String username, String password) {}

    private final AuthService authService;

    @PostMapping("/register")
    public ResponseEntity<String> register(@RequestBody User user) {
        authService.register(user);
        return ResponseEntity.ok("Usuario registrado correctamente.");
    }

    @PostMapping("/login")
    public Mono<Map<String, String>> login(@RequestBody AuthRequest user) {
        Map<String, String> tokens = authService.login(user.username(), user.password());
        return Mono.just(tokens);
    }

    @PostMapping("/refresh")
    public Mono<Map<String, String>> refreshToken(@RequestHeader("Authorization") String refreshToken) {
        String newAccessToken = authService.refreshAccessToken(refreshToken.replace("Bearer ", ""));
        Map<String, String> response = new HashMap<>();
        response.put("accessToken", newAccessToken);
        return Mono.just(response);
    }

}

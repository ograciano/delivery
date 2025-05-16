package com.delivery.auth.service;

import com.delivery.auth.model.Role;
import com.delivery.auth.model.User;
import com.delivery.auth.repository.UserRepository;
import com.delivery.auth.security.JwtProvider;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final UserRepository userRepository;
    private final JwtProvider jwtProvider;
    private final PasswordEncoder passwordEncoder;

    public void register(User user) {
        if (userRepository.existsByUsername(user.getUsername())) {
            throw new RuntimeException("El nombre de usuario ya existe.");
        }
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        userRepository.save(user);
    }

    public Map<String, String> login(String username, String password) {
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));

        if (!passwordEncoder.matches(password, user.getPassword())) {
            throw new RuntimeException("Credenciales inválidas");
        }

        String accessToken = jwtProvider.generateAccessToken(user.getUsername(), user.getRoles());
        String refreshToken = jwtProvider.generateRefreshToken(user.getUsername(), user.getRoles());

        Map<String, String> tokens = new HashMap<>();
        tokens.put("accessToken", accessToken);
        tokens.put("refreshToken", refreshToken);
        return tokens;
    }


    public Map<String, String> loginWithRefresh(String username, String password) {
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));

        if (!passwordEncoder.matches(password, user.getPassword())) {
            throw new RuntimeException("Credenciales inválidas");
        }

        List<String> roles = user.getRoles().stream()
                .map(Enum::name)
                .toList();

        Set<Role> rolesRefresh = user.getRoles().stream()
                .map(role -> Role.valueOf(role.name()))
                .collect(Collectors.toSet());

        String accessToken = jwtProvider.generateToken(username, roles);
        String refreshToken = jwtProvider.generateRefreshToken(username, rolesRefresh);

        Map<String, String> tokens = new HashMap<>();
        tokens.put("accessToken", accessToken);
        tokens.put("refreshToken", refreshToken);

        return tokens;
    }

    public String refreshAccessToken(String refreshToken) {
        if (!jwtProvider.validateRefreshToken(refreshToken)) {
            throw new RuntimeException("Refresh Token inválido");
        }

        String username = jwtProvider.getUsernameFromToken(refreshToken);
        List<String> roles = userRepository.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado"))
                .getRoles().stream()
                .map(Enum::name)
                .toList();

        return jwtProvider.generateToken(username, roles);
    }

}

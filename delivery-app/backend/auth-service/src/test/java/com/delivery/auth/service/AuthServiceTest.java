package com.delivery.auth.service;

import com.delivery.auth.model.Role;
import com.delivery.auth.model.User;
import com.delivery.auth.repository.UserRepository;
import com.delivery.auth.security.JwtProvider;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Map;
import java.util.Optional;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class AuthServiceTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private PasswordEncoder passwordEncoder;

    @Mock
    private JwtProvider jwtProvider;

    private AuthService authService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        authService = new AuthService(userRepository, jwtProvider, passwordEncoder);
    }

    @Test
    void loginReturnsTokensWhenCredentialsAreValid() {
        Set<Role> roles = Set.of(Role.CLIENT);
        User user = User.builder()
                .username("john")
                .password("hashed")
                .roles(roles)
                .build();

        when(userRepository.findByUsername("john")).thenReturn(Optional.of(user));
        when(passwordEncoder.matches("password", "hashed")).thenReturn(true);
        when(jwtProvider.generateAccessToken("john", roles)).thenReturn("access-token");
        when(jwtProvider.generateRefreshToken("john", roles)).thenReturn("refresh-token");

        Map<String, String> tokens = authService.login("john", "password");

        assertEquals("access-token", tokens.get("accessToken"));
        assertEquals("refresh-token", tokens.get("refreshToken"));
        assertEquals(2, tokens.size());
    }
}

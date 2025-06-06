package com.example.demo.service;

import com.example.demo.model.User;
import org.junit.jupiter.api.Test;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

class AuthServiceTest {
    private final AuthService authService = new AuthService();

    @Test
    void shouldAuthenticateValidUser() {
        Optional<User> result = authService.authenticate("admin@example.com", "admin123");
        assertTrue(result.isPresent());
        assertEquals("admin@example.com", result.get().getEmail());
    }

    @Test
    void shouldNotAuthenticateWithWrongPassword() {
        Optional<User> result = authService.authenticate("admin@example.com", "wrongpassword");
        assertTrue(result.isEmpty());
    }

    @Test
    void shouldNotAuthenticateNonexistentUser() {
        Optional<User> result = authService.authenticate("nonexistent@example.com", "password");
        assertTrue(result.isEmpty());
    }
} 
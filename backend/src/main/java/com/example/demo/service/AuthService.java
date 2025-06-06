package com.example.demo.service;

import com.example.demo.model.Role;
import com.example.demo.model.User;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

@Service
public class AuthService {
    private final Map<String, User> users = new HashMap<>();
    private final BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

    public AuthService() {
        // Initialize with some hardcoded users
        addUser("admin@example.com", "admin123", Role.ADMIN);
        addUser("user@example.com", "user123", Role.USER);
    }

    private void addUser(String email, String rawPassword, Role role) {
        String hashedPassword = passwordEncoder.encode(rawPassword);
        users.put(email, new User(email, hashedPassword, role));
    }

    public Optional<User> authenticate(String email, String password) {
        User user = users.get(email);
        if (user != null && passwordEncoder.matches(password, user.getPassword())) {
            return Optional.of(user);
        }
        return Optional.empty();
    }
} 
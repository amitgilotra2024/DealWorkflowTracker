package com.app.dealworkflowtracker.controller;

import com.app.dealworkflowtracker.dto.LoginRequest;
import com.app.dealworkflowtracker.dto.RegisterRequest;
import com.app.dealworkflowtracker.entities.Role;
import com.app.dealworkflowtracker.entities.User;
import com.app.dealworkflowtracker.repository.UserRepository;
import com.app.dealworkflowtracker.service.JwtService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.util.Collections;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;

    public AuthController(UserRepository userRepository,
                          PasswordEncoder passwordEncoder,
                          JwtService jwtService) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.jwtService = jwtService;
    }

    @PostMapping("/register")
    public ResponseEntity<?> register(@Valid @RequestBody RegisterRequest request) {
        if (userRepository.existsByUsername(request.getUsername())) {
            return ResponseEntity.badRequest().body(Map.of("error", "Username is already taken"));
        }
        if (userRepository.existsByEmail(request.getEmail())) {
            return ResponseEntity.badRequest().body(Map.of("error", "Email is already in use"));
        }

        User user = new User();
        user.setUsername(request.getUsername());
        user.setFirstName(request.getFirstName());
        user.setLastName(request.getLastName());
        user.setEmail(request.getEmail());
        user.setPassword(passwordEncoder.encode(request.getPassword()));
        user.setPostalCode(request.getPostalCode());

        userRepository.save(user);

        return ResponseEntity.status(HttpStatus.CREATED)
                .body(Map.of("message", "User registered successfully"));
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(@Valid @RequestBody LoginRequest request) {
        // Query database safely without throwing a 500 Unhandled Exception
        Optional<User> userOptional = userRepository.findByUsernameOrEmail(
                request.getUsernameOrEmail(),
                request.getUsernameOrEmail()
        );

        if (userOptional.isEmpty()) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body(Map.of("error", "Invalid username/email or password"));
        }

        User user = userOptional.get();

        System.out.println("Fetched User: " + user.getUsername());
        System.out.println("DB Password Hash: " + user.getPassword());
        System.out.println("Input Password: " + request.getPassword());
        System.out.println("Matches?: " + passwordEncoder.matches(request.getPassword(), user.getPassword()));

        String rawPassword = request.getPassword();
        String encodedPassword = new BCryptPasswordEncoder().encode(rawPassword);
        System.out.println("Generated Hash: " + encodedPassword);

        // Verify BCrypt hashed password against database string
        if (!passwordEncoder.matches(request.getPassword(), user.getPassword())) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body(Map.of("error", "Invalid username/email or password"));
        }

        // Safely extract role names for JWT claims, avoiding NullPointerException
        Set<String> roleNames = Optional.ofNullable(user.getRoles())
                .orElse(Collections.emptySet())
                .stream()
                .map(Role::getName)
                .filter(Objects::nonNull)
                .collect(Collectors.toSet());

        // Generate JWT token
        String token = jwtService.generateToken(user.getUsername(), roleNames);

        return ResponseEntity.ok(Map.of(
                "token", token,
                "type", "Bearer",
                "username", user.getUsername(),
                "email", user.getEmail()
        ));
    }
}
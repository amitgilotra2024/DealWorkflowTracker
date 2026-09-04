package com.app.dealworkflowtracker.service;

import com.app.dealworkflowtracker.dto.LoginRequest;
import com.app.dealworkflowtracker.entities.Role;
import com.app.dealworkflowtracker.entities.User;
import com.app.dealworkflowtracker.repository.UserRepository;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Set;
import java.util.stream.Collectors;

@Service
public class AuthService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;

    public AuthService(UserRepository userRepository, PasswordEncoder passwordEncoder, JwtService jwtService) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.jwtService = jwtService;
    }

    public String login(LoginRequest loginRequest) {
        // 1. Fetch user by username or email
        User user = userRepository.findByUsernameOrEmail(
                loginRequest.getUsernameOrEmail(),
                loginRequest.getUsernameOrEmail()
        ).orElseThrow(() -> new BadCredentialsException("Invalid username/email or password"));

        // 2. Validate password
        if (!passwordEncoder.matches(loginRequest.getPassword(), user.getPassword())) {
            throw new BadCredentialsException("Invalid username/email or password");
        }

        // 3. Extract role names
        Set<String> roles = user.getRoles().stream()
                .map(Role::getName)
                .collect(Collectors.toSet());

        // 4. Generate and return JWT token
        return jwtService.generateToken(user.getUsername(), roles);
    }
}

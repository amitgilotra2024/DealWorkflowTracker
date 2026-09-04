package com.app.dealworkflowtracker.dto;

import com.fasterxml.jackson.annotation.JsonAlias;
import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class LoginRequest {

    // Getters and Setters
    @NotBlank(message = "Username or Email is required")
    @JsonAlias({"username", "email", "username_or_email"}) // Accepts these JSON keys as aliases
    private String usernameOrEmail;

    @NotBlank(message = "Password is required")
    private String password;

}
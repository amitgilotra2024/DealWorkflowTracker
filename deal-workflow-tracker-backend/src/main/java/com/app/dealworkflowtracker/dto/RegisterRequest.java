package com.app.dealworkflowtracker.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;

public class RegisterRequest {

    @NotBlank
    @Getter @Setter
    private String username;

    @NotBlank
    @Getter @Setter
    private String firstName;

    @NotBlank
    @Getter @Setter
    private String lastName;

    @NotBlank
    @Email
    @Getter @Setter
    private String email;

    @NotBlank
    @Getter @Setter
    private String password;

    @Getter @Setter
    private String postalCode;
}
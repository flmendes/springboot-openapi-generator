package com.example.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;

public record StudentRequest(
    @NotBlank(message = "Name is required") String name,

    @NotBlank(message = "Email is required") @Email(message = "Email must be valid") String email,

    @NotBlank(message = "Phone is required") String phone
) {}

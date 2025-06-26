package com.shopverse.shopverse.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;

public record LoginRequest(
        @Email
        @NotBlank(message = "Email should not be empty")
        String email,
        @NotBlank(message = "Password should not be empty")
        String password
        ) {

}

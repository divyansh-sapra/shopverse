package com.shopverse.shopverse.dto;

import jakarta.validation.constraints.Email;

public record GetUserEmailRequest (
    @Email(message = "Invalid email")
    String email
){}
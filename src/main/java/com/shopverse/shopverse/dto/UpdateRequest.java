package com.shopverse.shopverse.dto;

import jakarta.validation.constraints.NotBlank;

public record UpdateRequest(
        @NotBlank
        String name,

        @NotBlank
        String email
) {
}

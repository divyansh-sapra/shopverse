package com.shopverse.shopverse.dto;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;

import java.math.BigDecimal;

public record ProductRequest(
        @NotBlank(message = "Product name cannot be empty")
        String name,
        @NotBlank(message = "Product description cannot be empty")
        String description,
        @DecimalMin(value = "0.01", message = "Product price must be greater than 0")
        BigDecimal price,
        @Min(value = 1L, message = "Product quantity must be greater than 0")
        int quantity
) {
}

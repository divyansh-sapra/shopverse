package com.shopverse.shopverse.dto;

import jakarta.validation.constraints.Min;

public record CartRequest(
        @Min(value = 1L, message = "Product Id is required")
        Long productId,

        @Min(value = 1L, message = "Minimum product quantity is 1")
        int quantity
) {}

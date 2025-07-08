package com.shopverse.shopverse.dto;

public record GetAllProductResponse(
        String name,
        java.math.BigDecimal price,
        int quantity
) {
}

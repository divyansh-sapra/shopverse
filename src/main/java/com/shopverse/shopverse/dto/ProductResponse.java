package com.shopverse.shopverse.dto;

import java.math.BigDecimal;

public record ProductResponse(
        String name,
        String description,
        BigDecimal price,
        int quantity
) {
}

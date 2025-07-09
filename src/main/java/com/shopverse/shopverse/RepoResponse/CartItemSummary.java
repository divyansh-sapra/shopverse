package com.shopverse.shopverse.RepoResponse;

import java.math.BigDecimal;

public interface CartItemSummary {
    BigDecimal getTotalAmount();

    int getProductIds();

    int getTotalQuantities();
}

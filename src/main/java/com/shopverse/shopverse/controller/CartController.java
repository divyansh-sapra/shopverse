package com.shopverse.shopverse.controller;

import com.shopverse.shopverse.dto.ApiResponse;
import com.shopverse.shopverse.dto.CartRequest;
import com.shopverse.shopverse.service.CartService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/cart")
public class CartController {
    private CartService cartService;

    public CartController(CartService cartService) {
        this.cartService = cartService;
    }

    @PostMapping("/add-to-cart")
    public ResponseEntity<ApiResponse<String>> addToCart(@Valid @RequestBody CartRequest cartRequest) {
        boolean isAdded = cartService.addToCart(cartRequest);
        return isAdded?ResponseEntity.ok(new ApiResponse<>("Success", "", "Product to cart added successfully")):ResponseEntity.ok(new ApiResponse<>("Failed", "Product to cart is not added please try again", null));
    }
}

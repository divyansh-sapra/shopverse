package com.shopverse.shopverse.dto;

public record LoginResponse(
        String message,
        String token,
        String role
        ) {

}

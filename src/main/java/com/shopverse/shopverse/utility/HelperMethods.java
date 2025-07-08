package com.shopverse.shopverse.utility;

import com.shopverse.shopverse.entity.User;
import com.shopverse.shopverse.repository.UserRepository;
import com.shopverse.shopverse.security.JwtService;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class HelperMethods {

    private UserRepository userRepository;
    private HttpServletRequest request;
    private JwtService jwtService;

    public HelperMethods(UserRepository userRepository, HttpServletRequest request, JwtService jwtService) {
        this.userRepository = userRepository;
        this.request = request;
        this.jwtService = jwtService;
    }

    public Optional<User> getUserByToken() {
        String token = extractTokenFromHeader();
        Long userId = jwtService.extractId(token);
        return userRepository.findById(userId);

    }

    private String extractTokenFromHeader() {
        String authHeader = request.getHeader("Authorization");
        if (authHeader != null && authHeader.startsWith("Bearer ")) {
            return authHeader.substring(7); // Remove "Bearer " prefix
        }

        return null;
    }
}

package com.shopverse.shopverse.security;

import com.shopverse.shopverse.repository.UserRepository;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.List;

@Component
public class JwtAuthFilter extends OncePerRequestFilter {

    private final JwtService jwtService;
    private final UserRepository userRepo;
    private final TokenBlacklistService tokenBlacklistService;

    public JwtAuthFilter(JwtService jwtService, UserRepository userRepo, TokenBlacklistService tokenBlacklistService) {
        this.jwtService = jwtService;
        this.userRepo = userRepo;
        this.tokenBlacklistService = tokenBlacklistService;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response,
                                    FilterChain filterChain)
            throws ServletException, IOException {

        final String authHeader = request.getHeader("Authorization");

        // No token or doesn't start with Bearer
        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            filterChain.doFilter(request, response);
            return;
        }

        // Extract token and email
        String token = authHeader.substring(7); // Remove "Bearer "

        if(tokenBlacklistService.isTokenBlacklist(token)){
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            return;
        }

        String email = jwtService.extractEmail(token);

        // Check if email is not null and not already authenticated
        if (email != null && SecurityContextHolder.getContext().getAuthentication() == null) {
            var user = userRepo.findByEmail(email)
                    .orElse(null);

            if (user != null) {
                var authToken = new UsernamePasswordAuthenticationToken(
                        user, null, List.of(() -> "ROLE_" + user.getRole())
                );
                authToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));

                SecurityContextHolder.getContext().setAuthentication(authToken);
            }
        }

        filterChain.doFilter(request, response);
    }
}

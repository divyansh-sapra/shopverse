package com.shopverse.shopverse.security;

import java.util.Date;

import org.springframework.stereotype.Service;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;

@Service
public class JwtService {

    private static final String SECRET_KEY = "asjfbaksfaskdfaksdfkasd.askndlfna.-qalsnasd";
    private static final long EXPIRATION_TIME_MS = 24 * 60 * 60 * 1000;

    private final Algorithm algorithm = Algorithm.HMAC256(SECRET_KEY);

    public String generateToken(String email, String role) {
        return JWT.create()
                .withSubject(email)
                .withClaim("role", role)
                .withIssuedAt(new Date())
                .withExpiresAt(new Date(System.currentTimeMillis() + EXPIRATION_TIME_MS))
                .sign(algorithm);
    }

    public String extractEmail(String token) {
        return JWT.require(algorithm)
                .build().verify(token).getSubject();
    }

    public String extractRole(String token) {
        return JWT.require(algorithm)
                .build().verify(token).getClaim("role").asString();
    }
}

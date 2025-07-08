package com.shopverse.shopverse.security;

import java.util.Date;

import org.springframework.stereotype.Service;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;

@Service
public class JwtService {

    private static final String SECRET_KEY = "asjfbaksfaskdfaksdfkasd.askndlfna.-qalsnasd";
    private static final long EXPIRATION_TIME_MS = 60 * 60L * 1000;

    private final Algorithm algorithm = Algorithm.HMAC256(SECRET_KEY);

    public String generateToken(String email, String role, Long id) {
        return JWT.create()
                .withSubject(email)
                .withClaim("id", id)
                .withClaim("role", role)
                .withIssuedAt(new Date())
                .withExpiresAt(new Date(System.currentTimeMillis() + EXPIRATION_TIME_MS))
                .sign(algorithm);
    }

    public String generateRefreshToken(String email) {
        return JWT.create()
                .withSubject(email)
                .withExpiresAt(new Date(System.currentTimeMillis() + 7 * 24 * 60 * 60 * 1000))
                .sign(algorithm);
    }

    public String extractEmail(String token) {
        return JWT.require(algorithm)
                .build().verify(token).getSubject();
    }

    public Long extractId(String token) {
        return  JWT.require(algorithm).build().verify(token).getClaim("id").asLong();
    }

    public String extractRole(String token) {
        return JWT.require(algorithm)
                .build().verify(token).getClaim("role").asString();
    }

    public long getTokenExpiryMillis(String token) {
        Date expiresAt = JWT.require(algorithm).build().verify(token).getExpiresAt();
        return expiresAt.getTime() - System.currentTimeMillis();
    }

    public boolean isTokenValid(String token, String email) {
        try {
            String subject = extractEmail(token);
            return subject.equals(email) && !isTokenExpired(token);
        } catch (Exception e) {
            return false;
        }
    }

    public boolean isTokenExpired(String token) {
        return JWT.require(algorithm).build().verify(token).getExpiresAt().before(new Date());
    }
}

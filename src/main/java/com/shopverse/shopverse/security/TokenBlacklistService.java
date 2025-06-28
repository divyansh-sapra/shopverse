package com.shopverse.shopverse.security;

import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;

import java.time.Duration;

@Service
public class TokenBlacklistService {

    private final StringRedisTemplate redisTemplate;

    public TokenBlacklistService(StringRedisTemplate redisTemplate) {
        this.redisTemplate = redisTemplate;
    }

    public void blacklistToken(String token, long expirationMillis){
        Duration duration = Duration.ofMillis(expirationMillis);
        redisTemplate.opsForValue().set(token, "blacklisted", duration);
    }

    public boolean isTokenBlacklist(String token) {
        return redisTemplate.hasKey(token);
    }
}

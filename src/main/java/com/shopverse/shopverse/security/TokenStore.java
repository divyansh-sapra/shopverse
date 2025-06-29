package com.shopverse.shopverse.security;

import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;

import java.time.Duration;

@Service
public class TokenStore {

    StringRedisTemplate redisTemplate;

    public TokenStore(StringRedisTemplate  redisTemplate){
        this.redisTemplate=redisTemplate;
    }

    public void storeRefreshToken(String email, String refreshToken){
        Duration duration = Duration.ofMillis(3600000);
        redisTemplate.opsForValue().set("refresh: "+email, refreshToken, duration);
    }

    public boolean isRefreshTokenValid(String email, String refreshToken) {
        String token = redisTemplate.opsForValue().get("refresh:" + email);
        return token!=null && token.equals(refreshToken);
    }
}

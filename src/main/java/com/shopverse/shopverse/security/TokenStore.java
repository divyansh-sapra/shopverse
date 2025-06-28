package com.shopverse.shopverse.security;

import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;

@Service
public class TokenStore {

    StringRedisTemplate redisTemplate;

    public TokenStore(StringRedisTemplate  redisTemplate){
        this.redisTemplate=redisTemplate;
    }

    public void storeRefreshToken(String email, String refreshToken){
        redisTemplate.opsForValue().set("refresh: "+email, refreshToken);
    }

    public boolean isRefreshTokenValid(String email, String refreshToken) {
        String token = redisTemplate.opsForValue().get("refresh:" + email);
        return token!=null && token.equals(refreshToken);
    }
}

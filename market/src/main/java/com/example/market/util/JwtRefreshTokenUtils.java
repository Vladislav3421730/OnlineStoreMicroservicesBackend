package com.example.market.util;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;

import java.time.Duration;
import java.util.UUID;

@Component
@RequiredArgsConstructor
public class JwtRefreshTokenUtils {

    private final RedisTemplate<String, String> redisTemplate;

    @Value("${jwt.refresh.lifetime}")
    private Duration jwtLifetime;

    public String generateRefreshToken(String email) {
        String token = UUID.randomUUID().toString();
        redisTemplate.opsForValue().set(token,email, jwtLifetime);
        return token;
    }

    public boolean isRefreshTokenValid(String token) {
        return redisTemplate.hasKey(token);
    }

    public String getEmailFromRefreshToken(String token) {
        return redisTemplate.opsForValue().get(token);
    }

}

package com.gominitta.backend.global.auth.jwt;

import java.time.Duration;
import java.util.Optional;

import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Repository;

import lombok.RequiredArgsConstructor;

@Repository
@RequiredArgsConstructor
public class RefreshTokenStore {

	private static final String TOKEN_KEY_PREFIX = "refresh:";
	private static final String USER_KEY_PREFIX = "user_refresh:";

	private final RedisTemplate<String, String> redisTemplate;

	public void save(String refreshToken, Long userId, Duration ttl) {
		redisTemplate.opsForValue().set(TOKEN_KEY_PREFIX + refreshToken, String.valueOf(userId), ttl);
		redisTemplate.opsForValue().set(USER_KEY_PREFIX + userId, refreshToken, ttl);
	}

	public Optional<Long> findUserIdByToken(String refreshToken) {
		String value = redisTemplate.opsForValue().get(TOKEN_KEY_PREFIX + refreshToken);
		if (value == null) {
			return Optional.empty();
		}
		return Optional.of(Long.parseLong(value));
	}

	public boolean exists(String refreshToken) {
		return Boolean.TRUE.equals(redisTemplate.hasKey(TOKEN_KEY_PREFIX + refreshToken));
	}

	public void delete(String refreshToken) {
		String userId = redisTemplate.opsForValue().get(TOKEN_KEY_PREFIX + refreshToken);
		redisTemplate.delete(TOKEN_KEY_PREFIX + refreshToken);
		if (userId != null) {
			redisTemplate.delete(USER_KEY_PREFIX + userId);
		}
	}

	public void deleteByUserId(Long userId) {
		String refreshToken = redisTemplate.opsForValue().get(USER_KEY_PREFIX + userId);
		redisTemplate.delete(USER_KEY_PREFIX + userId);
		if (refreshToken != null) {
			redisTemplate.delete(TOKEN_KEY_PREFIX + refreshToken);
		}
	}
}

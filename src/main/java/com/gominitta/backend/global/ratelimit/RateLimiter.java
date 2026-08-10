package com.gominitta.backend.global.ratelimit;

import java.time.Duration;

import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;

import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class RateLimiter {

	private final RedisTemplate<String, String> redisTemplate;

	public boolean isAllowed(String key, int limit, Duration window) {
		Long count = redisTemplate.opsForValue().increment(key);
		if (count == null) {
			return true;
		}
		if (count == 1L) {
			redisTemplate.expire(key, window);
		}
		return count <= limit;
	}
}

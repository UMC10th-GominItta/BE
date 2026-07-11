package com.gominitta.backend.global.auth.jwt;

import java.util.Date;
import java.util.UUID;

import javax.crypto.SecretKey;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import jakarta.annotation.PostConstruct;

@Component
public class JwtUtil {

	@Value("${jwt.secret}")
	private String secret;

	@Value("${jwt.access-token-expiration}")
	private long accessTokenExpiration;

	private SecretKey secretKey;

	@PostConstruct
	public void init() {
		this.secretKey = Keys.hmacShaKeyFor(Decoders.BASE64.decode(secret));
	}

	public String generateAccessToken(Long userId) {
		return Jwts.builder()
			.subject(String.valueOf(userId))
			.issuedAt(new Date())
			.expiration(new Date(System.currentTimeMillis() + accessTokenExpiration))
			.signWith(secretKey)
			.compact();
	}

	public String generateRefreshToken() {
		return UUID.randomUUID().toString();
	}

	// JwtException (만료 포함) 은 호출부(Filter)에서 처리
	public Long extractUserId(String token) {
		return Long.parseLong(
			Jwts.parser()
				.verifyWith(secretKey)
				.build()
				.parseSignedClaims(token)
				.getPayload()
				.getSubject()
		);
	}
}

package com.gominitta.backend.global.auth.jwt;

import java.io.IOException;
import java.util.Collections;

import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.gominitta.backend.domain.auth.exception.AuthErrorCode;
import com.gominitta.backend.global.common.response.ApiResponse;

import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.JwtException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class JwtAuthenticationFilter extends OncePerRequestFilter {

	private final JwtUtil jwtUtil;
	private final ObjectMapper objectMapper;

	@Override
	protected void doFilterInternal(
		HttpServletRequest request,
		HttpServletResponse response,
		FilterChain filterChain
	) throws ServletException, IOException {
		String token = extractToken(request);

		if (StringUtils.hasText(token)) {
			try {
				Long userId = jwtUtil.extractUserId(token);
				UsernamePasswordAuthenticationToken authentication =
					new UsernamePasswordAuthenticationToken(userId, null, Collections.emptyList());
				SecurityContextHolder.getContext().setAuthentication(authentication);
			} catch (ExpiredJwtException ex) {
				writeErrorResponse(response, AuthErrorCode.EXPIRED_ACCESS_TOKEN);
				return;
			} catch (JwtException | IllegalArgumentException ex) {
				writeErrorResponse(response, AuthErrorCode.UNAUTHORIZED);
				return;
			}
		}

		filterChain.doFilter(request, response);
	}

	private String extractToken(HttpServletRequest request) {
		String bearerToken = request.getHeader("Authorization");
		if (StringUtils.hasText(bearerToken) && bearerToken.startsWith("Bearer ")) {
			return bearerToken.substring(7);
		}
		return null;
	}

	private void writeErrorResponse(HttpServletResponse response, AuthErrorCode errorCode) throws IOException {
		response.setContentType("application/json;charset=UTF-8");
		response.setStatus(errorCode.getStatus().value());
		response.getWriter().write(
			objectMapper.writeValueAsString(
				ApiResponse.fail(errorCode.getCode(), errorCode.getMessage())
			)
		);
	}
}

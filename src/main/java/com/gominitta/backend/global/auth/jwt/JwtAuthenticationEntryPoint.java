package com.gominitta.backend.global.auth.jwt;

import java.io.IOException;

import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.gominitta.backend.domain.auth.exception.AuthErrorCode;
import com.gominitta.backend.global.common.response.ApiResponse;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class JwtAuthenticationEntryPoint implements AuthenticationEntryPoint {

	private final ObjectMapper objectMapper;

	@Override
	public void commence(
		HttpServletRequest request,
		HttpServletResponse response,
		AuthenticationException authException
	) throws IOException {
		response.setContentType("application/json;charset=UTF-8");
		response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
		AuthErrorCode errorCode = AuthErrorCode.UNAUTHORIZED;
		response.getWriter().write(
			objectMapper.writeValueAsString(
				ApiResponse.fail(errorCode.getCode(), errorCode.getMessage())
			)
		);
	}
}

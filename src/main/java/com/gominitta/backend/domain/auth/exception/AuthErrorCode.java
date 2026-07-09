package com.gominitta.backend.domain.auth.exception;

import org.springframework.http.HttpStatus;

import com.gominitta.backend.global.common.exception.BaseErrorCode;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum AuthErrorCode implements BaseErrorCode {

	// 400 Bad Request
	MISSING_KAKAO_TOKEN(HttpStatus.BAD_REQUEST, "AUTH_40001", "카카오 토큰이 없습니다."),
	MISSING_REFRESH_TOKEN(HttpStatus.BAD_REQUEST, "AUTH_40002", "Refresh Token이 없습니다."),

	// 401 Unauthorized
	UNAUTHORIZED(HttpStatus.UNAUTHORIZED, "AUTH_40101", "인증이 필요합니다."),
	INVALID_KAKAO_TOKEN(HttpStatus.UNAUTHORIZED, "AUTH_40102", "유효하지 않은 카카오 토큰입니다."),
	INVALID_TOKEN_ORIGIN(HttpStatus.UNAUTHORIZED, "AUTH_40103", "허용되지 않은 앱에서 발급된 토큰입니다."),
	EXPIRED_ACCESS_TOKEN(HttpStatus.UNAUTHORIZED, "AUTH_40104", "만료된 Access Token입니다."),
	INVALID_REFRESH_TOKEN(HttpStatus.UNAUTHORIZED, "AUTH_40105", "유효하지 않은 Refresh Token입니다."),
	REFRESH_TOKEN_MISMATCH(HttpStatus.UNAUTHORIZED, "AUTH_40106", "Refresh Token이 일치하지 않습니다."),

	// 404 Not Found
	USER_NOT_FOUND(HttpStatus.NOT_FOUND, "AUTH_40401", "사용자를 찾을 수 없습니다."),

	// 502 Bad Gateway
	KAKAO_TOKEN_VERIFY_FAILED(HttpStatus.BAD_GATEWAY, "AUTH_50201", "카카오 토큰 검증에 실패했습니다."),
	KAKAO_USERINFO_REQUEST_FAILED(HttpStatus.BAD_GATEWAY, "AUTH_50202", "카카오 사용자 정보 요청에 실패했습니다."),
	KAKAO_EMAIL_NOT_PROVIDED(HttpStatus.BAD_GATEWAY, "AUTH_50203", "카카오 계정의 이메일 정보를 가져올 수 없습니다.");

	private final HttpStatus status;
	private final String code;
	private final String message;
}

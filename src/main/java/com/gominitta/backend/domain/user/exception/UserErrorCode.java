package com.gominitta.backend.domain.user.exception;

import org.springframework.http.HttpStatus;

import com.gominitta.backend.global.common.exception.BaseErrorCode;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum UserErrorCode implements BaseErrorCode {

	// 400 Bad Request
	BAD_REQUEST(HttpStatus.BAD_REQUEST, "USER_40001", "잘못된 요청입니다."),

	// 401 Unauthorized
	UNAUTHORIZED(HttpStatus.UNAUTHORIZED, "USER_40101", "로그인이 필요한 요청입니다."),

	// 404 Not Found
	USER_NOT_FOUND(HttpStatus.NOT_FOUND, "USER_40401", "사용자를 찾을 수 없습니다."),

	// 500 Internal Server Error
	INTERNAL_ERROR(HttpStatus.INTERNAL_SERVER_ERROR, "USER_50001", "처리 중 서버 오류가 발생했습니다.");

	private final HttpStatus status;
	private final String code;
	private final String message;
}

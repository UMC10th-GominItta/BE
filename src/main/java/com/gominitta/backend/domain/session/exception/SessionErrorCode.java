package com.gominitta.backend.domain.session.exception;

import org.springframework.http.HttpStatus;

import com.gominitta.backend.global.common.exception.BaseErrorCode;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum SessionErrorCode implements BaseErrorCode {

	// 400 Bad Request
	BAD_REQUEST(HttpStatus.BAD_REQUEST, "SESSION_40001", "잘못된 요청입니다."),
	INVALID_STATUS_VALUE(HttpStatus.BAD_REQUEST, "SESSION_40002", "잘못된 세션 상태 값입니다."),

	// 401 Unauthorized
	UNAUTHORIZED(HttpStatus.UNAUTHORIZED, "SESSION_40101", "로그인이 필요한 요청입니다."),

	// 403 Forbidden
	FORBIDDEN(HttpStatus.FORBIDDEN, "SESSION_40301", "접근 권한이 없습니다."),

	// 404 Not Found
	SESSION_NOT_FOUND(HttpStatus.NOT_FOUND, "SESSION_40401", "세션을 찾을 수 없습니다."),

	// 409 Conflict
	INVALID_STATUS_TRANSITION(HttpStatus.CONFLICT, "SESSION_40901", "변경할 수 없는 세션 상태입니다."),

	// 422 Unprocessable Entity
	EMOTION_SCORE_REQUIRED(HttpStatus.UNPROCESSABLE_ENTITY, "SESSION_42201", "세션 후 감정 점수가 필요합니다."),

	// 500 Internal Server Error
	INTERNAL_ERROR(HttpStatus.INTERNAL_SERVER_ERROR, "SESSION_50001", "처리 중 서버 오류가 발생했습니다.");

	private final HttpStatus status;
	private final String code;
	private final String message;
}

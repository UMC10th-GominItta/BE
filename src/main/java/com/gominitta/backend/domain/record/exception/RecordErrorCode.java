package com.gominitta.backend.domain.record.exception;

import org.springframework.http.HttpStatus;

import com.gominitta.backend.global.common.exception.BaseErrorCode;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum RecordErrorCode implements BaseErrorCode {

	// 400 Bad Request
	EMPTY_CONTENT(HttpStatus.BAD_REQUEST, "RECORD_40001", "기록 내용이 비어 있습니다."),

	// 403 Forbidden
	FORBIDDEN(HttpStatus.FORBIDDEN, "RECORD_40301", "접근 권한이 없습니다."),

	// 404 Not Found
	RECORD_NOT_FOUND(HttpStatus.NOT_FOUND, "RECORD_40401", "기록을 찾을 수 없습니다.");

	private final HttpStatus status;
	private final String code;
	private final String message;
}

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
	CONTENT_TOO_LONG(HttpStatus.BAD_REQUEST, "RECORD_40002", "기록 내용은 3000자를 초과할 수 없습니다."),
	EMPTY_FILE(HttpStatus.BAD_REQUEST, "RECORD_40003", "음성 파일이 없습니다."),

	// 403 Forbidden
	FORBIDDEN(HttpStatus.FORBIDDEN, "RECORD_40301", "접근 권한이 없습니다."),

	// 404 Not Found
	RECORD_NOT_FOUND(HttpStatus.NOT_FOUND, "RECORD_40401", "기록을 찾을 수 없습니다."),

	// 413 Payload Too Large
	FILE_TOO_LARGE(HttpStatus.PAYLOAD_TOO_LARGE, "RECORD_41301", "파일 용량이 초과되었습니다."),

	// 415 Unsupported Media Type
	UNSUPPORTED_FILE_TYPE(HttpStatus.UNSUPPORTED_MEDIA_TYPE, "RECORD_41501", "지원하지 않는 파일 형식입니다."),

	// 422 Unprocessable Entity
	STT_FAILED(HttpStatus.UNPROCESSABLE_ENTITY, "RECORD_42201", "음성 변환에 실패했습니다."),

	// 500 Internal Server Error
	INTERNAL_ERROR(HttpStatus.INTERNAL_SERVER_ERROR, "RECORD_50001", "처리 중 서버 오류가 발생했습니다.");

	private final HttpStatus status;
	private final String code;
	private final String message;
}

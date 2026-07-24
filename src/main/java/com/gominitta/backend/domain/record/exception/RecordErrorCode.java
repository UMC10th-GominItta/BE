package com.gominitta.backend.domain.record.exception;

import org.springframework.http.HttpStatus;

import com.gominitta.backend.global.common.exception.BaseErrorCode;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum RecordErrorCode implements BaseErrorCode {

	// 400 Bad Request
	EMPTY_CONTENT(HttpStatus.BAD_REQUEST, "RECORD_40001", "기록 내용이 비어 있습니다.");

	private final HttpStatus status;
	private final String code;
	private final String message;
}

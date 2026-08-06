package com.gominitta.backend.domain.report.exception;

import org.springframework.http.HttpStatus;

import com.gominitta.backend.global.common.exception.BaseErrorCode;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum ReportErrorCode implements BaseErrorCode {

	INVALID_PERIOD(HttpStatus.BAD_REQUEST, "REPORT_400_1", "유효하지 않은 조회 기간입니다.");

	private final HttpStatus status;
	private final String code;
	private final String message;
}

package com.gominitta.backend.domain.dailymessage.exception;

import org.springframework.http.HttpStatus;

import com.gominitta.backend.global.common.exception.BaseErrorCode;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum DailyMessageErrorCode implements BaseErrorCode {

	// 404 Not Found
	DAILY_MESSAGE_NOT_FOUND(HttpStatus.NOT_FOUND, "DM_40401", "오늘의 한마디를 찾을 수 없습니다.");

	private final HttpStatus status;
	private final String code;
	private final String message;
}

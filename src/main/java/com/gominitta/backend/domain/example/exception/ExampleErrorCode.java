package com.gominitta.backend.domain.example.exception;

import org.springframework.http.HttpStatus;

import com.gominitta.backend.global.common.exception.BaseErrorCode;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum ExampleErrorCode implements BaseErrorCode {

	EXAMPLE_NOT_FOUND(HttpStatus.NOT_FOUND, "EXAMPLE_404", "예제를 찾을 수 없습니다.");

	private final HttpStatus status;
	private final String code;
	private final String message;
}

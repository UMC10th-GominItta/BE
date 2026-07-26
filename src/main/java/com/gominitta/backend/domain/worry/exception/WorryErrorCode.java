package com.gominitta.backend.domain.worry.exception;

import org.springframework.http.HttpStatus;

import com.gominitta.backend.global.common.exception.BaseErrorCode;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum WorryErrorCode implements BaseErrorCode {

    // 403 Forbidden
    FORBIDDEN(HttpStatus.FORBIDDEN, "WORRY_40301", "접근 권한이 없습니다."),

    // 404 Not Found
    WORRY_NOT_FOUND(HttpStatus.NOT_FOUND, "WORRY_40401", "걱정을 찾을 수 없습니다."),

    // 500 Internal Server Error
    INTERNAL_ERROR(HttpStatus.INTERNAL_SERVER_ERROR, "WORRY_50001", "처리 중 서버 오류가 발생했습니다.");

    private final HttpStatus status;
    private final String code;
    private final String message;
}

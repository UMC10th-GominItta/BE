package com.gominitta.backend.domain.favortietimeslot.exception;

import com.gominitta.backend.global.common.exception.BaseErrorCode;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;


@Getter
@RequiredArgsConstructor
public enum FavoriteTimeErrorCode implements BaseErrorCode {

// 403 Forbidden
FORBIDDEN(HttpStatus.FORBIDDEN, "FAVORITETIME_40301", "접근 권한이 없습니다."),

// 404 Not Found
FAVORITETIME_NOT_FOUND(HttpStatus.NOT_FOUND, "FAVORITETIME_40401", "즐겨찾는시간을 찾을 수 없습니다."),

// 500 Internal Server Error
INTERNAL_ERROR(HttpStatus.INTERNAL_SERVER_ERROR, "FAVORITETIME_50001", "처리 중 서버 오류가 발생했습니다.");

private final HttpStatus status;
private final String code;
private final String message;
}




package com.gominitta.backend.domain.notification.exception;

import org.springframework.http.HttpStatus;

import com.gominitta.backend.global.common.exception.BaseErrorCode;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum NotificationErrorCode implements BaseErrorCode {

	NOTIFICATION_FORBIDDEN(HttpStatus.FORBIDDEN, "NOTIFICATION_40301", "본인의 알림만 조회할 수 있습니다."),

	NOTIFICATION_NOT_FOUND(HttpStatus.NOT_FOUND, "NOTIFICATION_40401", "알림을 찾을 수 없습니다.");

	private final HttpStatus status;
	private final String code;
	private final String message;
}
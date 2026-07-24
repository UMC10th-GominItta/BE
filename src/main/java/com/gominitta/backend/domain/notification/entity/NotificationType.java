package com.gominitta.backend.domain.notification.entity;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum NotificationType {

	SESSION_REMINDER("예약한 마음 세션 시작 시간 알림"),
	SESSION_DONE("마음 세션 완료 알림"),
	REPORT_READY("마음 리포트 준비 완료 알림"),
	SYSTEM("시스템 공지");

	private final String description;
}

package com.gominitta.backend.domain.notification.dto.request;

public record NotificationSettingUpdateRequestDTO(
	Boolean worryReminderEnabled,
	Boolean sessionStartEnabled
) {
}

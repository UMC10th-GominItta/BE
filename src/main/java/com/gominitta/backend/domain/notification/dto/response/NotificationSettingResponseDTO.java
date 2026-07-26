package com.gominitta.backend.domain.notification.dto.response;

import com.gominitta.backend.domain.notification.entity.NotificationSetting;

public record NotificationSettingResponseDTO(
	boolean worryReminderEnabled,
	boolean sessionStartEnabled
) {
	public static NotificationSettingResponseDTO from(NotificationSetting setting) {
		return new NotificationSettingResponseDTO(
			setting.isWorryReminderEnabled(),
			setting.isSessionStartEnabled()
		);
	}
}

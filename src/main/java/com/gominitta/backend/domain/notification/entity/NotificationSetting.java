package com.gominitta.backend.domain.notification.entity;

import com.gominitta.backend.global.common.entity.BaseEntity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(name = "notification_settings")
public class NotificationSetting extends BaseEntity {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@Column(name = "user_id", nullable = false, unique = true)
	private Long userId;

	@Column(name = "worry_reminder_enabled", nullable = false)
	private boolean worryReminderEnabled;

	@Column(name = "session_start_enabled", nullable = false)
	private boolean sessionStartEnabled;

	@Builder
	private NotificationSetting(Long userId, boolean worryReminderEnabled, boolean sessionStartEnabled) {
		this.userId = userId;
		this.worryReminderEnabled = worryReminderEnabled;
		this.sessionStartEnabled = sessionStartEnabled;
	}

	// 신규 유저 기본값: 둘 다 ON
	public static NotificationSetting createDefault(Long userId) {
		return NotificationSetting.builder()
			.userId(userId)
			.worryReminderEnabled(true)
			.sessionStartEnabled(true)
			.build();
	}

	public void update(Boolean worryReminderEnabled, Boolean sessionStartEnabled) {
		if (worryReminderEnabled != null) {
			this.worryReminderEnabled = worryReminderEnabled;
		}
		if (sessionStartEnabled != null) {
			this.sessionStartEnabled = sessionStartEnabled;
		}
	}
}

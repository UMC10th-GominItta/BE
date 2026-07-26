package com.gominitta.backend.domain.notification.entity;

import java.time.LocalDateTime;

import com.gominitta.backend.global.common.entity.BaseEntity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Entity
@Table(name = "notifications")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Notification extends BaseEntity {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "notification_id")
	private Long id;

	@Column(name = "user_id", nullable = false)
	private Long userId;

	@Enumerated(EnumType.STRING)
	@Column(name = "type", nullable = false, length = 30)
	private NotificationType type;

	@Column(name = "title", nullable = false, length = 100)
	private String title;

	@Column(name = "body", length = 500)
	private String body;

	@Column(name = "is_read", nullable = false)
	private boolean isRead;

	@Column(name = "read_at")
	private LocalDateTime readAt;

	@Builder
	private Notification(Long userId, NotificationType type, String title, String body) {
		this.userId = userId;
		this.type = type;
		this.title = title;
		this.body = body;
		this.isRead = false;
		this.readAt = null;
	}

	// 읽음 처리
	public void markAsRead() {
		if (this.isRead) {
			return;
		}
		this.isRead = true;
		this.readAt = LocalDateTime.now();
	}

	// user 검증용
	public boolean isOwnedBy(Long userId) {
		return this.userId.equals(userId);
	}
}

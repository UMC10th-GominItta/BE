package com.gominitta.backend.domain.notification.dto.response;

import java.time.LocalDateTime;

import com.gominitta.backend.domain.notification.entity.Notification;
import com.gominitta.backend.domain.notification.entity.NotificationType;

// 알림 단건 응답 DTO
public record NotificationResponseDTO(
	Long notificationId,
	NotificationType type,
	String title,
	String body,
	boolean isRead,
	LocalDateTime readAt,
	LocalDateTime createdAt
) {
	public static NotificationResponseDTO from(Notification n) {
		return new NotificationResponseDTO(
			n.getId(),
			n.getType(),
			n.getTitle(),
			n.getBody(),
			n.isRead(),
			n.getReadAt(),
			n.getCreatedAt()
		);
	}
}

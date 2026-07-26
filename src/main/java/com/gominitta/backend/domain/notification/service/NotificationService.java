package com.gominitta.backend.domain.notification.service;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.gominitta.backend.domain.notification.dto.response.NotificationListResponseDTO;
import com.gominitta.backend.domain.notification.dto.response.NotificationResponseDTO;
import com.gominitta.backend.domain.notification.entity.Notification;
import com.gominitta.backend.domain.notification.exception.NotificationErrorCode;
import com.gominitta.backend.domain.notification.repository.NotificationRepository;
import com.gominitta.backend.global.common.exception.GeneralException;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class NotificationService {

	private final NotificationRepository notificationRepository;

	// 알림 목록 조회
	public NotificationListResponseDTO getNotifications(Long userId, Pageable pageable) {
		Page<NotificationResponseDTO> page = notificationRepository.findByUserIdAndIsDeletedFalseOrderByCreatedAtDesc(
			userId, pageable).map(NotificationResponseDTO::from);
		return NotificationListResponseDTO.from(page);
	}

	/**
	 * 알림 읽음 처리.
	 * - 존재하지 않거나 삭제된 알림이면 404
	 * - 본인 알림이 아니면 403
	 * - 이미 읽은 알림은 멱등 처리
	 */
	@Transactional
	public NotificationResponseDTO readNotification(Long userId, Long notificationId) {
		Notification notification = notificationRepository.findByIdAndIsDeletedFalse(notificationId)
			.orElseThrow(() -> new GeneralException(NotificationErrorCode.NOTIFICATION_NOT_FOUND));

		if (!notification.isOwnedBy(userId)) {
			throw new GeneralException(NotificationErrorCode.NOTIFICATION_FORBIDDEN);
		}

		notification.markAsRead();
		return NotificationResponseDTO.from(notification);
	}
}

package com.gominitta.backend.domain.notification.repository;

import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import com.gominitta.backend.domain.notification.entity.Notification;
import com.gominitta.backend.domain.notification.entity.NotificationType;
import com.gominitta.backend.domain.notification.entity.ReferenceType;

public interface NotificationRepository extends JpaRepository<Notification, Long> {

	// 본인 알림만 최신순으로 페이지 조회
	Page<Notification> findByUserIdAndIsDeletedFalseOrderByCreatedAtDesc(Long userId, Pageable pageable);

	// 삭제되지 않은 알림 단건 조회
	Optional<Notification> findByIdAndIsDeletedFalse(Long notificationId);

	boolean existsByReferenceTypeAndReferenceIdAndType(
		ReferenceType referenceType, Long referenceId, NotificationType type);
}

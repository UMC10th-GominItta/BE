package com.gominitta.backend.domain.notification.dto.response;

import java.util.List;

import org.springframework.data.domain.Page;

// 알림 목록 응답 DTO
public record NotificationListResponseDTO(
	List<NotificationResponseDTO> notifications,
	int page,
	int size,
	long totalElements,
	int totalPages,
	boolean hasNext
) {
	public static NotificationListResponseDTO from(Page<NotificationResponseDTO> page) {
		return new NotificationListResponseDTO(
			page.getContent(),
			page.getNumber(),
			page.getSize(),
			page.getTotalElements(),
			page.getTotalPages(),
			page.hasNext()
		);
	}
}

package com.gominitta.backend.domain.notification.controller;

import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.gominitta.backend.domain.notification.dto.response.NotificationListResponseDTO;
import com.gominitta.backend.domain.notification.dto.response.NotificationResponseDTO;
import com.gominitta.backend.domain.notification.service.NotificationService;
import com.gominitta.backend.global.common.response.ApiResponse;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;

@Tag(name = "Notification", description = "알림 API")
@RestController
@RequestMapping("/api/v1/notifications")
@RequiredArgsConstructor
public class NotificationController {

	private final NotificationService notificationService;

	@Operation(summary = "알림 목록 조회", description = "본인의 알림을 최신순으로 페이지 조회합니다.")
	@GetMapping
	public ApiResponse<NotificationListResponseDTO> getNotifications(
		@PageableDefault(size = 20, sort = "createdAt", direction = Sort.Direction.DESC) Pageable pageable
	) {
		Long userId = CurrentUserProvider.getCurrentUserId();
		return ApiResponse.success(notificationService.getNotifications(userId, pageable));
	}

	@Operation(summary = "알림 읽음 처리", description = "알림을 읽음 상태로 변경합니다.")
	@PatchMapping("/{notificationId}")
	public ApiResponse<NotificationResponseDTO> readNotification(
		@PathVariable Long notificationId
	) {
		Long userId = CurrentUserProvider.getCurrentUserId();
		return ApiResponse.success(notificationService.readNotification(userId, notificationId));
	}
}

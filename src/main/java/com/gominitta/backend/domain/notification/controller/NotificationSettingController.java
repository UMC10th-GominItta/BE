package com.gominitta.backend.domain.notification.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.gominitta.backend.domain.notification.dto.request.NotificationSettingUpdateRequestDTO;
import com.gominitta.backend.domain.notification.dto.response.NotificationSettingResponseDTO;
import com.gominitta.backend.domain.notification.service.NotificationSettingService;
import com.gominitta.backend.global.common.response.ApiResponse;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;

@Tag(name = "Notification", description = "알림 API")
@RestController
@RequestMapping("/api/v1/notifications/settings")
@RequiredArgsConstructor
public class NotificationSettingController {

	private final NotificationSettingService settingService;

	@Operation(summary = "알림 설정 조회", description = "본인의 푸시 알림 설정을 조회합니다.")
	@GetMapping
	public ApiResponse<NotificationSettingResponseDTO> getSettings() {
		Long userId = CurrentUserProvider.getCurrentUserId();
		return ApiResponse.success(settingService.getSettings(userId));
	}

	@Operation(summary = "알림 설정 변경", description = "걱정 예약 리마인드 / 마음 세션 시작 알림 토글을 변경합니다.")
	@PatchMapping
	public ApiResponse<NotificationSettingResponseDTO> updateSettings(
		@RequestBody NotificationSettingUpdateRequestDTO request
	) {
		Long userId = CurrentUserProvider.getCurrentUserId();
		return ApiResponse.success(settingService.updateSettings(userId, request));
	}
}

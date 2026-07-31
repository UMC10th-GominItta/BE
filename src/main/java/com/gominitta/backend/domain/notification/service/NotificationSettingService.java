package com.gominitta.backend.domain.notification.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.gominitta.backend.domain.notification.dto.request.NotificationSettingUpdateRequestDTO;
import com.gominitta.backend.domain.notification.dto.response.NotificationSettingResponseDTO;
import com.gominitta.backend.domain.notification.entity.NotificationSetting;
import com.gominitta.backend.domain.notification.repository.NotificationSettingRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class NotificationSettingService {

	private final NotificationSettingRepository settingRepository;

	@Transactional
	public NotificationSettingResponseDTO getSettings(Long userId) {
		NotificationSetting setting = getOrCreate(userId);
		return NotificationSettingResponseDTO.from(setting);
	}

	@Transactional
	public NotificationSettingResponseDTO updateSettings(Long userId, NotificationSettingUpdateRequestDTO request) {
		NotificationSetting setting = getOrCreate(userId);
		setting.update(request.worryReminderEnabled(), request.sessionStartEnabled());
		return NotificationSettingResponseDTO.from(setting);
	}

	// 설정 row 없는 유저는 조회/변경 시점에 기본값으로 자동 생성
	private NotificationSetting getOrCreate(Long userId) {
		return settingRepository.findByUserId(userId)
			.orElseGet(() -> settingRepository.save(NotificationSetting.createDefault(userId)));
	}
}

package com.gominitta.backend.domain.user.service;

import java.time.Duration;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.gominitta.backend.domain.dailymessage.entity.DailyMessage;
import com.gominitta.backend.domain.dailymessage.service.DailyMessageService;
import com.gominitta.backend.domain.session.entity.MindSession;
import com.gominitta.backend.domain.session.entity.enums.SessionStatus;
import com.gominitta.backend.domain.session.repository.SessionRepository;
import com.gominitta.backend.domain.user.dto.request.UserUpdateRequestDTO;
import com.gominitta.backend.domain.user.dto.response.HomeResponseDTO;
import com.gominitta.backend.domain.user.dto.response.UserProfileResponseDTO;
import com.gominitta.backend.domain.user.dto.response.UserUpdateResponseDTO;
import com.gominitta.backend.domain.user.entity.User;
import com.gominitta.backend.domain.user.exception.UserErrorCode;
import com.gominitta.backend.domain.user.repository.UserRepository;
import com.gominitta.backend.global.auth.jwt.RefreshTokenStore;
import com.gominitta.backend.global.common.exception.GeneralException;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class UserService {

	@Value("${jwt.access-token-expiration}")
	private long accessTokenExpiration;

	private final UserRepository userRepository;
	private final DailyMessageService dailyMessageService;
	private final SessionRepository sessionRepository;
	private final RefreshTokenStore refreshTokenRepository;

	@Transactional(readOnly = true)
	public UserProfileResponseDTO getMyProfile(Long userId) {
		User user = findActiveUser(userId);
		return UserProfileResponseDTO.from(user);
	}

	@Transactional
	public UserUpdateResponseDTO updateMyProfile(Long userId, UserUpdateRequestDTO request) {
		User user = findActiveUser(userId);

		if (request.nickname() != null) {
			user.updateNickname(request.nickname());
		}
		if (request.profileIcon() != null) {
			user.updateProfileIcon(request.profileIcon());
		}

		return UserUpdateResponseDTO.from(user);
	}

	@Transactional
	public void deleteMe(Long userId) {
		User user = findActiveUser(userId);
		refreshTokenRepository.deleteByUserId(userId);
		refreshTokenRepository.markDeleted(userId, Duration.ofMillis(accessTokenExpiration));
		user.deactivate();
	}

	@Transactional
	public HomeResponseDTO getHome(Long userId) {
		User user = findActiveUser(userId);
		DailyMessage dailyMessage = dailyMessageService.getTodaysMessage();

		List<SessionStatus> activeStatuses = List.of(SessionStatus.SCHEDULED, SessionStatus.INCOMPLETE);
		Optional<MindSession> nearestSession = sessionRepository
			.findByUserIdAndStatusInOrderByScheduledStartAtAsc(userId, activeStatuses)
			.stream()
			.findFirst();

		return HomeResponseDTO.of(user, dailyMessage, nearestSession);
	}

	private User findActiveUser(Long userId) {
		return userRepository.findByIdAndIsDeletedFalse(userId)
			.orElseThrow(() -> new GeneralException(UserErrorCode.USER_NOT_FOUND));
	}
}

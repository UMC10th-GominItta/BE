package com.gominitta.backend.domain.user.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.gominitta.backend.domain.dailymessage.entity.DailyMessage;
import com.gominitta.backend.domain.dailymessage.service.DailyMessageService;
import com.gominitta.backend.domain.user.dto.request.UserUpdateRequestDTO;
import com.gominitta.backend.domain.user.dto.response.HomeResponseDTO;
import com.gominitta.backend.domain.user.dto.response.UserProfileResponseDTO;
import com.gominitta.backend.domain.user.dto.response.UserUpdateResponseDTO;
import com.gominitta.backend.domain.user.entity.User;
import com.gominitta.backend.domain.user.exception.UserErrorCode;
import com.gominitta.backend.domain.user.repository.UserRepository;
import com.gominitta.backend.global.common.exception.GeneralException;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class UserService {

	private final UserRepository userRepository;
	private final DailyMessageService dailyMessageService;

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
		user.deactivate();
	}

	@Transactional
	public HomeResponseDTO getHome(Long userId) {
		User user = findActiveUser(userId);
		DailyMessage dailyMessage = dailyMessageService.getTodaysMessage();

		// TODO: mind_sessions 도메인 구현 후 아래 작업 필요
		//  1. MindSessionRepository에 현재 시간 기준 가장 가까운 세션 조회 쿼리 추가
		//     ex) findTopByUserIdAndScheduledStartAtAfterOrderByScheduledStartAtAsc(userId, now)
		//  2. 조회 결과를 HomeResponseDTO.of()에 파라미터로 전달
		//  3. HomeResponseDTO.of() 시그니처에 MindSessionInfo 파라미터 추가

		return HomeResponseDTO.of(user, dailyMessage);
	}

	private User findActiveUser(Long userId) {
		return userRepository.findByIdAndIsDeletedFalse(userId)
			.orElseThrow(() -> new GeneralException(UserErrorCode.USER_NOT_FOUND));
	}
}

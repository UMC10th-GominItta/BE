package com.gominitta.backend.domain.session.service;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.gominitta.backend.domain.session.dto.response.SessionListResponseDTO;
import com.gominitta.backend.domain.session.entity.enums.SessionStatus;
import com.gominitta.backend.domain.session.exception.SessionErrorCode;
import com.gominitta.backend.domain.session.repository.SessionRepository;
import com.gominitta.backend.global.common.exception.GeneralException;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class SessionService {

	private static final List<SessionStatus> DEFAULT_STATUSES =
		List.of(SessionStatus.SCHEDULED, SessionStatus.INCOMPLETE);

	private final SessionRepository sessionRepository;

	@Transactional(readOnly = true)
	public List<SessionListResponseDTO> getSessions(Long userId, String status) {
		List<SessionStatus> statuses = resolveStatuses(status);
		return sessionRepository.findByUserIdAndStatusInOrderByScheduledStartAtAsc(userId, statuses).stream()
			.map(SessionListResponseDTO::from)
			.toList();
	}

	private List<SessionStatus> resolveStatuses(String status) {
		if (status == null) {
			return DEFAULT_STATUSES;
		}

		SessionStatus parsed = parseStatus(status);
		if (parsed != SessionStatus.SCHEDULED && parsed != SessionStatus.INCOMPLETE) {
			throw new GeneralException(SessionErrorCode.BAD_REQUEST);
		}
		return List.of(parsed);
	}

	private SessionStatus parseStatus(String status) {
		try {
			return SessionStatus.valueOf(status.toUpperCase());
		} catch (IllegalArgumentException e) {
			throw new GeneralException(SessionErrorCode.BAD_REQUEST);
		}
	}
}

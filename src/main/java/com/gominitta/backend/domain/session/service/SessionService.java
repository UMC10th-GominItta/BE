package com.gominitta.backend.domain.session.service;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.gominitta.backend.domain.record.entity.SessionRecord;
import com.gominitta.backend.domain.record.repository.SessionRecordRepository;
import com.gominitta.backend.domain.session.dto.request.SessionStatusChangeRequestDTO;
import com.gominitta.backend.domain.session.dto.response.SessionDetailResponseDTO;
import com.gominitta.backend.domain.session.dto.response.SessionListResponseDTO;
import com.gominitta.backend.domain.session.dto.response.SessionStatusChangeResponseDTO;
import com.gominitta.backend.domain.session.entity.MindSession;
import com.gominitta.backend.domain.session.entity.enums.SessionStatus;
import com.gominitta.backend.domain.session.entity.enums.ThemeCategory;
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
	private final SessionRecordRepository sessionRecordRepository;

	@Transactional(readOnly = true)
	public List<SessionListResponseDTO> getSessions(Long userId, String status) {
		List<SessionStatus> statuses = resolveStatuses(status);
		return sessionRepository.findByUserIdAndStatusInOrderByScheduledStartAtAsc(userId, statuses).stream()
			.map(SessionListResponseDTO::from)
			.toList();
	}

	@Transactional(readOnly = true)
	public SessionDetailResponseDTO getSessionDetail(Long userId, Long sessionId) {
		MindSession session = findSessionById(sessionId);
		if (!session.getUserId().equals(userId)) {
			throw new GeneralException(SessionErrorCode.FORBIDDEN);
		}

		List<SessionRecord> records = sessionRecordRepository.findBySessionIdOrderByCreatedAtAsc(sessionId);
		return SessionDetailResponseDTO.of(session, records);
	}

	@Transactional
	public SessionStatusChangeResponseDTO changeStatus(
		Long userId, Long sessionId, SessionStatusChangeRequestDTO request
	) {
		MindSession session = findSessionById(sessionId);
		if (!session.getUserId().equals(userId)) {
			throw new GeneralException(SessionErrorCode.FORBIDDEN);
		}

		SessionStatus targetStatus = parseTargetStatus(request.status());

		if (targetStatus == SessionStatus.IN_PROGRESS) {
			if (!session.isStartable()) {
				throw new GeneralException(SessionErrorCode.INVALID_STATUS_TRANSITION);
			}
			session.start();
		} else {
			if (!session.isCompletable()) {
				throw new GeneralException(SessionErrorCode.INVALID_STATUS_TRANSITION);
			}
			if (request.emotionScoreAfter() == null) {
				throw new GeneralException(SessionErrorCode.EMOTION_SCORE_REQUIRED);
			}
			session.complete(request.emotionScoreAfter());
		}

		return SessionStatusChangeResponseDTO.from(session);
	}

	/**
	 * 걱정(worry) 예약 시 B 도메인이 자신의 트랜잭션 안에서 호출하는 내부 메서드. REST API로 노출되지 않음.
	 * B가 넘긴 예약 시점 값을 세션에 스냅샷으로 저장해서, 이후 세션 조회는 B 재호출 없이 자체 컬럼으로 응답한다.
	 */
	@Transactional
	public Long createSessionForWorry(Long worryId, Long userId, LocalDateTime scheduledStartAt,
			LocalDateTime scheduledEndAt, String worryContent, ThemeCategory themeCategory,
			Integer emotionScoreBefore) {
		if (sessionRepository.existsByWorryId(worryId)) {
			throw new GeneralException(SessionErrorCode.SESSION_ALREADY_EXISTS);
		}

		MindSession session = MindSession.create(userId, worryId, worryContent, themeCategory,
			emotionScoreBefore, scheduledStartAt, scheduledEndAt);
		return sessionRepository.save(session).getMindSessionId();
	}

	private SessionStatus parseTargetStatus(String status) {
		SessionStatus parsed;
		try {
			parsed = SessionStatus.valueOf(status.toUpperCase());
		} catch (IllegalArgumentException e) {
			throw new GeneralException(SessionErrorCode.INVALID_STATUS_VALUE);
		}

		if (parsed != SessionStatus.IN_PROGRESS && parsed != SessionStatus.COMPLETED) {
			throw new GeneralException(SessionErrorCode.INVALID_STATUS_VALUE);
		}
		return parsed;
	}

	private MindSession findSessionById(Long sessionId) {
		return sessionRepository.findById(sessionId)
			.orElseThrow(() -> new GeneralException(SessionErrorCode.SESSION_NOT_FOUND));
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

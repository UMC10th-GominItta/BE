package com.gominitta.backend.domain.record.service;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.gominitta.backend.domain.record.dto.response.SessionRecordResponseDTO;
import com.gominitta.backend.domain.record.entity.SessionRecord;
import com.gominitta.backend.domain.record.entity.enums.RecordType;
import com.gominitta.backend.domain.record.repository.SessionRecordRepository;
import com.gominitta.backend.domain.session.entity.MindSession;
import com.gominitta.backend.domain.session.exception.SessionErrorCode;
import com.gominitta.backend.domain.session.repository.SessionRepository;
import com.gominitta.backend.global.common.exception.GeneralException;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class RecordService {

	private final SessionRepository sessionRepository;
	private final SessionRecordRepository sessionRecordRepository;

	@Transactional(readOnly = true)
	public List<SessionRecordResponseDTO> getSessionRecords(Long userId, Long sessionId, String recordType) {
		MindSession session = findSessionById(sessionId);
		if (!session.getUserId().equals(userId)) {
			throw new GeneralException(SessionErrorCode.FORBIDDEN);
		}

		List<SessionRecord> records = (recordType == null)
			? sessionRecordRepository.findBySessionIdOrderByCreatedAtAsc(sessionId)
			: sessionRecordRepository.findBySessionIdAndRecordTypeOrderByCreatedAtAsc(
				sessionId, parseRecordType(recordType));

		return records.stream().map(SessionRecordResponseDTO::from).toList();
	}

	private RecordType parseRecordType(String recordType) {
		try {
			return RecordType.valueOf(recordType.toUpperCase());
		} catch (IllegalArgumentException e) {
			throw new GeneralException(SessionErrorCode.BAD_REQUEST);
		}
	}

	private MindSession findSessionById(Long sessionId) {
		return sessionRepository.findById(sessionId)
			.orElseThrow(() -> new GeneralException(SessionErrorCode.SESSION_NOT_FOUND));
	}
}

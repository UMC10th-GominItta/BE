package com.gominitta.backend.domain.record.service;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.gominitta.backend.domain.record.dto.request.TextRecordCreateRequestDTO;
import com.gominitta.backend.domain.record.dto.response.SessionRecordResponseDTO;
import com.gominitta.backend.domain.record.entity.SessionRecord;
import com.gominitta.backend.domain.record.entity.enums.RecordType;
import com.gominitta.backend.domain.record.exception.RecordErrorCode;
import com.gominitta.backend.domain.record.repository.SessionRecordRepository;
import com.gominitta.backend.domain.session.entity.MindSession;
import com.gominitta.backend.domain.session.entity.enums.SessionStatus;
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

	@Transactional
	public SessionRecordResponseDTO createTextRecord(Long userId, Long sessionId, TextRecordCreateRequestDTO request) {
		MindSession session = findSessionById(sessionId);
		if (!session.getUserId().equals(userId)) {
			throw new GeneralException(SessionErrorCode.FORBIDDEN);
		}
		if (request.contentText() == null || request.contentText().isBlank()) {
			throw new GeneralException(RecordErrorCode.EMPTY_CONTENT);
		}
		if (session.getStatus() == SessionStatus.COMPLETED) {
			throw new GeneralException(SessionErrorCode.RECORDING_NOT_ALLOWED);
		}

		SessionRecord record = SessionRecord.create(sessionId, RecordType.TEXT, request.contentText(), null);
		return SessionRecordResponseDTO.from(sessionRecordRepository.save(record));
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

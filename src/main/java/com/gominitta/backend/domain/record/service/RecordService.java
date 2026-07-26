package com.gominitta.backend.domain.record.service;

import java.util.List;
import java.util.Set;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import com.gominitta.backend.domain.record.client.OpenAiSttClient;
import com.gominitta.backend.domain.record.dto.request.RecordUpdateRequestDTO;
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
import com.gominitta.backend.global.storage.FileStorage;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class RecordService {

	private static final int MAX_CONTENT_LENGTH = 3000;
	private static final Set<String> ALLOWED_VOICE_EXTENSIONS =
		Set.of("mp3", "mp4", "mpeg", "mpga", "m4a", "wav", "webm");

	private final SessionRepository sessionRepository;
	private final SessionRecordRepository sessionRecordRepository;
	private final OpenAiSttClient openAiSttClient;
	private final FileStorage fileStorage;

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
		if (request.contentText().length() > MAX_CONTENT_LENGTH) {
			throw new GeneralException(RecordErrorCode.CONTENT_TOO_LONG);
		}
		if (session.getStatus() == SessionStatus.COMPLETED) {
			throw new GeneralException(SessionErrorCode.RECORDING_NOT_ALLOWED);
		}

		SessionRecord record = SessionRecord.create(sessionId, RecordType.TEXT, request.contentText(), null);
		return SessionRecordResponseDTO.from(sessionRecordRepository.save(record));
	}

	@Transactional
	public SessionRecordResponseDTO createVoiceRecord(Long userId, Long sessionId, MultipartFile file) {
		MindSession session = findSessionById(sessionId);
		if (!session.getUserId().equals(userId)) {
			throw new GeneralException(SessionErrorCode.FORBIDDEN);
		}
		if (file == null || file.isEmpty()) {
			throw new GeneralException(RecordErrorCode.EMPTY_FILE);
		}
		validateVoiceFileExtension(file.getOriginalFilename());
		if (session.getStatus() == SessionStatus.COMPLETED) {
			throw new GeneralException(SessionErrorCode.RECORDING_NOT_ALLOWED);
		}

		String contentText = openAiSttClient.transcribe(file);
		String mediaUrl = fileStorage.store(file, "voice");

		SessionRecord record = SessionRecord.create(sessionId, RecordType.VOICE, contentText, mediaUrl);
		return SessionRecordResponseDTO.from(sessionRecordRepository.save(record));
	}

	@Transactional
	public SessionRecordResponseDTO updateRecord(
		Long userId, Long sessionId, Long recordId, RecordUpdateRequestDTO request
	) {
		MindSession session = findSessionById(sessionId);
		SessionRecord record = findRecordById(recordId);
		if (!record.getSessionId().equals(sessionId)) {
			throw new GeneralException(RecordErrorCode.RECORD_NOT_FOUND);
		}
		if (!session.getUserId().equals(userId)) {
			throw new GeneralException(RecordErrorCode.FORBIDDEN);
		}
		if (request.contentText() == null || request.contentText().isBlank()) {
			throw new GeneralException(RecordErrorCode.EMPTY_CONTENT);
		}
		if (request.contentText().length() > MAX_CONTENT_LENGTH) {
			throw new GeneralException(RecordErrorCode.CONTENT_TOO_LONG);
		}

		record.updateContent(request.contentText());
		sessionRecordRepository.saveAndFlush(record);
		return SessionRecordResponseDTO.from(record);
	}

	@Transactional
	public void deleteRecord(Long userId, Long sessionId, Long recordId) {
		MindSession session = findSessionById(sessionId);
		SessionRecord record = findRecordById(recordId);
		if (!record.getSessionId().equals(sessionId)) {
			throw new GeneralException(RecordErrorCode.RECORD_NOT_FOUND);
		}
		if (!session.getUserId().equals(userId)) {
			throw new GeneralException(RecordErrorCode.FORBIDDEN);
		}

		sessionRecordRepository.delete(record);
	}

	private SessionRecord findRecordById(Long recordId) {
		return sessionRecordRepository.findById(recordId)
			.orElseThrow(() -> new GeneralException(RecordErrorCode.RECORD_NOT_FOUND));
	}

	private void validateVoiceFileExtension(String filename) {
		if (filename == null || filename.lastIndexOf('.') == -1) {
			throw new GeneralException(RecordErrorCode.UNSUPPORTED_FILE_TYPE);
		}
		String extension = filename.substring(filename.lastIndexOf('.') + 1).toLowerCase();
		if (!ALLOWED_VOICE_EXTENSIONS.contains(extension)) {
			throw new GeneralException(RecordErrorCode.UNSUPPORTED_FILE_TYPE);
		}
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

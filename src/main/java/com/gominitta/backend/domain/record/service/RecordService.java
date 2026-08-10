package com.gominitta.backend.domain.record.service;

import java.io.IOException;
import java.io.InputStream;
import java.time.Duration;
import java.util.Arrays;
import java.util.List;
import java.util.Set;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import com.gominitta.backend.domain.record.client.OpenAiOcrClient;
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
import com.gominitta.backend.global.ratelimit.RateLimiter;
import com.gominitta.backend.global.storage.FileStorage;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class RecordService {

	private static final int MAX_CONTENT_LENGTH = 3000;
	private static final long MAX_IMAGE_SIZE_BYTES = 10L * 1024 * 1024;
	private static final int MAGIC_BYTES_HEADER_LENGTH = 16;
	private static final int UPLOAD_RATE_LIMIT = 10;
	private static final Duration UPLOAD_RATE_LIMIT_WINDOW = Duration.ofMinutes(1);

	private static final Set<String> ALLOWED_VOICE_EXTENSIONS =
		Set.of("mp3", "mp4", "mpeg", "mpga", "m4a", "wav", "webm");
	private static final Set<String> ALLOWED_IMAGE_EXTENSIONS = Set.of("png", "jpg", "jpeg");

	private static final Set<String> ALLOWED_VOICE_MIME_TYPES = Set.of(
		"audio/mpeg", "audio/mp4", "audio/wav", "audio/x-wav", "audio/wave", "audio/webm"
	);
	private static final Set<String> ALLOWED_IMAGE_MIME_TYPES = Set.of("image/png", "image/jpeg");

	private final SessionRepository sessionRepository;
	private final SessionRecordRepository sessionRecordRepository;
	private final OpenAiSttClient openAiSttClient;
	private final OpenAiOcrClient openAiOcrClient;
	private final FileStorage fileStorage;
	private final RateLimiter rateLimiter;

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

		return records.stream().map(this::toResponse).toList();
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
		return toResponse(sessionRecordRepository.save(record));
	}

	@Transactional
	public SessionRecordResponseDTO createVoiceRecord(Long userId, Long sessionId, MultipartFile file) {
		MindSession session = findSessionById(sessionId);
		if (!session.getUserId().equals(userId)) {
			throw new GeneralException(SessionErrorCode.FORBIDDEN);
		}
		checkUploadRateLimit(userId);
		if (file == null || file.isEmpty()) {
			throw new GeneralException(RecordErrorCode.EMPTY_FILE);
		}
		String extension = extractAndValidateExtension(file.getOriginalFilename(), ALLOWED_VOICE_EXTENSIONS);
		validateMimeType(file.getContentType(), ALLOWED_VOICE_MIME_TYPES);
		validateMagicBytes(file, extension);
		if (session.getStatus() == SessionStatus.COMPLETED) {
			throw new GeneralException(SessionErrorCode.RECORDING_NOT_ALLOWED);
		}

		String contentText = openAiSttClient.transcribe(file);
		String mediaKey = fileStorage.store(file, "voice");

		SessionRecord record = SessionRecord.create(sessionId, RecordType.VOICE, contentText, mediaKey);
		return toResponse(sessionRecordRepository.save(record));
	}

	@Transactional
	public SessionRecordResponseDTO createHandwritingRecord(Long userId, Long sessionId, MultipartFile file) {
		MindSession session = findSessionById(sessionId);
		if (!session.getUserId().equals(userId)) {
			throw new GeneralException(SessionErrorCode.FORBIDDEN);
		}
		checkUploadRateLimit(userId);
		if (file == null || file.isEmpty()) {
			throw new GeneralException(RecordErrorCode.EMPTY_IMAGE_FILE);
		}
		if (file.getSize() > MAX_IMAGE_SIZE_BYTES) {
			throw new GeneralException(RecordErrorCode.FILE_TOO_LARGE);
		}
		String extension = extractAndValidateExtension(file.getOriginalFilename(), ALLOWED_IMAGE_EXTENSIONS);
		validateMimeType(file.getContentType(), ALLOWED_IMAGE_MIME_TYPES);
		validateMagicBytes(file, extension);
		if (session.getStatus() == SessionStatus.COMPLETED) {
			throw new GeneralException(SessionErrorCode.RECORDING_NOT_ALLOWED);
		}

		String contentText = openAiOcrClient.extractText(file);
		String mediaKey = fileStorage.store(file, "handwriting");

		SessionRecord record = SessionRecord.create(sessionId, RecordType.HANDWRITING, contentText, mediaKey);
		return toResponse(sessionRecordRepository.save(record));
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
		return toResponse(record);
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

	private SessionRecordResponseDTO toResponse(SessionRecord record) {
		String resolvedMediaUrl = record.getMediaUrl() == null ? null : fileStorage.resolveUrl(record.getMediaUrl());
		return SessionRecordResponseDTO.from(record, resolvedMediaUrl);
	}

	private SessionRecord findRecordById(Long recordId) {
		return sessionRecordRepository.findById(recordId)
			.orElseThrow(() -> new GeneralException(RecordErrorCode.RECORD_NOT_FOUND));
	}

	private void checkUploadRateLimit(Long userId) {
		String key = "rate-limit:record-upload:" + userId;
		if (!rateLimiter.isAllowed(key, UPLOAD_RATE_LIMIT, UPLOAD_RATE_LIMIT_WINDOW)) {
			throw new GeneralException(RecordErrorCode.RATE_LIMIT_EXCEEDED);
		}
	}

	private String extractAndValidateExtension(String filename, Set<String> allowedExtensions) {
		if (filename == null || filename.lastIndexOf('.') == -1) {
			throw new GeneralException(RecordErrorCode.UNSUPPORTED_FILE_TYPE);
		}
		String extension = filename.substring(filename.lastIndexOf('.') + 1).toLowerCase();
		if (!allowedExtensions.contains(extension)) {
			throw new GeneralException(RecordErrorCode.UNSUPPORTED_FILE_TYPE);
		}
		return extension;
	}

	private void validateMimeType(String contentType, Set<String> allowedMimeTypes) {
		if (contentType == null || !allowedMimeTypes.contains(contentType.toLowerCase())) {
			throw new GeneralException(RecordErrorCode.UNSUPPORTED_FILE_TYPE);
		}
	}

	private void validateMagicBytes(MultipartFile file, String extension) {
		byte[] header = readHeaderBytes(file);
		if (!hasValidMagicBytes(header, extension)) {
			throw new GeneralException(RecordErrorCode.UNSUPPORTED_FILE_TYPE);
		}
	}

	private byte[] readHeaderBytes(MultipartFile file) {
		try (InputStream inputStream = file.getInputStream()) {
			byte[] buffer = new byte[MAGIC_BYTES_HEADER_LENGTH];
			int read = inputStream.read(buffer);
			return read > 0 ? Arrays.copyOf(buffer, read) : new byte[0];
		} catch (IOException e) {
			throw new GeneralException(RecordErrorCode.UNSUPPORTED_FILE_TYPE);
		}
	}

	private boolean hasValidMagicBytes(byte[] header, String extension) {
		return switch (extension) {
			case "png" -> startsWith(header, 0x89, 0x50, 0x4E, 0x47);
			case "jpg", "jpeg" -> startsWith(header, 0xFF, 0xD8, 0xFF);
			case "wav" -> matchesAscii(header, 0, "RIFF") && matchesAscii(header, 8, "WAVE");
			case "m4a", "mp4" -> matchesAscii(header, 4, "ftyp");
			case "mp3" -> matchesAscii(header, 0, "ID3")
				|| (header.length > 1 && (header[0] & 0xFF) == 0xFF && (header[1] & 0xE0) == 0xE0);
			// mpeg/mpga/webm have no single reliable simple signature; rely on extension + MIME check instead.
			default -> true;
		};
	}

	private boolean startsWith(byte[] header, int... expected) {
		if (header.length < expected.length) {
			return false;
		}
		for (int i = 0; i < expected.length; i++) {
			if ((header[i] & 0xFF) != expected[i]) {
				return false;
			}
		}
		return true;
	}

	private boolean matchesAscii(byte[] header, int offset, String expected) {
		if (header.length < offset + expected.length()) {
			return false;
		}
		for (int i = 0; i < expected.length(); i++) {
			if (header[offset + i] != (byte) expected.charAt(i)) {
				return false;
			}
		}
		return true;
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

package com.gominitta.backend.domain.session.dto.response;

import java.time.LocalDateTime;

import com.gominitta.backend.domain.session.entity.SessionRecord;
import com.gominitta.backend.domain.session.entity.enums.RecordType;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;

@Builder
@Schema(description = "세션 기록 응답")
public record SessionRecordResponseDTO(
	@Schema(description = "기록 ID", example = "5") Long id,
	@Schema(description = "세션 ID", example = "1") Long sessionId,
	@Schema(description = "기록 유형", example = "text") RecordType recordType,
	@Schema(description = "텍스트 내용", example = "지금 드는 생각을 그대로 적었어요.") String contentText,
	@Schema(description = "미디어 URL", example = "https://cdn.gominitta.com/records/voice/6.m4a") String mediaUrl,
	@Schema(description = "기록 생성 시각", example = "2026-05-27T22:10:00") LocalDateTime createdAt
) {
	public static SessionRecordResponseDTO from(SessionRecord record) {
		return SessionRecordResponseDTO.builder()
			.id(record.getSessionRecordId())
			.sessionId(record.getSessionId())
			.recordType(record.getRecordType())
			.contentText(record.getContentText())
			.mediaUrl(record.getMediaUrl())
			.createdAt(record.getCreatedAt())
			.build();
	}
}

package com.gominitta.backend.domain.session.dto.response;

import java.time.LocalDateTime;
import java.util.List;
import java.util.function.Function;

import com.gominitta.backend.domain.record.entity.SessionRecord;
import com.gominitta.backend.domain.record.entity.enums.RecordType;
import com.gominitta.backend.domain.session.entity.MindSession;
import com.gominitta.backend.domain.session.entity.enums.SessionStatus;
import com.gominitta.backend.domain.session.entity.enums.ThemeCategory;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;

@Builder
@Schema(description = "세션 상세 응답")
public record SessionDetailResponseDTO(
	@Schema(description = "세션 ID", example = "1") Long id,
	@Schema(description = "걱정 ID", example = "10") Long worryId,
	@Schema(description = "걱정 제목 (스냅샷)", example = "취업 걱정") String worryTitle,
	@Schema(description = "걱정 내용 (스냅샷)", example = "UMC 프론트가 안 구해지면 어떡하지") String worryContent,
	@Schema(description = "걱정 주제", example = "진로") ThemeCategory themeCategory,
	@Schema(description = "세션 상태", example = "scheduled") SessionStatus status,
	@Schema(description = "세션 예정 시작 시각", example = "2026-05-27T22:00:00") LocalDateTime scheduledStartAt,
	@Schema(description = "세션 예정 종료 시각", example = "2026-05-27T23:00:00") LocalDateTime scheduledEndAt,
	@Schema(description = "세션 시작 시각", example = "null") LocalDateTime startedAt,
	@Schema(description = "세션 완료 시각", example = "null") LocalDateTime completedAt,
	@Schema(description = "세션 전 감정 점수", example = "8") Integer emotionScoreBefore,
	@Schema(description = "세션 후 감정 점수", example = "null") Integer emotionScoreAfter,
	@Schema(description = "세션 기록 목록") List<SessionRecordInfo> records
) {
	public static SessionDetailResponseDTO of(
		MindSession session, List<SessionRecord> records, Function<String, String> mediaUrlResolver
	) {
		return SessionDetailResponseDTO.builder()
			.id(session.getMindSessionId())
			.worryId(session.getWorryId())
			.worryTitle(session.getWorryTitle())
			.worryContent(session.getWorryContent())
			.themeCategory(session.getThemeCategory())
			.status(session.getStatus())
			.scheduledStartAt(session.getScheduledStartAt())
			.scheduledEndAt(session.getScheduledEndAt())
			.startedAt(session.getStartedAt())
			.completedAt(session.getCompletedAt())
			.emotionScoreBefore(session.getEmotionScoreBefore())
			.emotionScoreAfter(session.getEmotionScoreAfter())
			.records(records.stream().map(record -> SessionRecordInfo.from(record, mediaUrlResolver)).toList())
			.build();
	}

	@Builder
	@Schema(description = "세션 기록 정보")
	public record SessionRecordInfo(
		@Schema(description = "기록 ID", example = "5") Long id,
		@Schema(description = "기록 유형", example = "text") RecordType recordType,
		@Schema(description = "텍스트 내용", example = "지금 드는 생각을 그대로 적었어요.") String contentText,
		@Schema(description = "미디어 URL", example = "null") String mediaUrl,
		@Schema(description = "기록 생성 시각", example = "2026-05-27T22:10:00") LocalDateTime createdAt
	) {
		public static SessionRecordInfo from(SessionRecord record, Function<String, String> mediaUrlResolver) {
			String resolvedMediaUrl = record.getMediaUrl() == null ? null : mediaUrlResolver.apply(record.getMediaUrl());
			return SessionRecordInfo.builder()
				.id(record.getSessionRecordId())
				.recordType(record.getRecordType())
				.contentText(record.getContentText())
				.mediaUrl(resolvedMediaUrl)
				.createdAt(record.getCreatedAt())
				.build();
		}
	}
}

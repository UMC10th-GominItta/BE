package com.gominitta.backend.domain.session.dto.response;

import java.time.LocalDateTime;

import com.gominitta.backend.domain.session.entity.MindSession;
import com.gominitta.backend.domain.session.entity.enums.SessionStatus;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;

@Builder
@Schema(description = "세션 상태 변경 응답")
public record SessionStatusChangeResponseDTO(
	@Schema(description = "세션 ID", example = "1") Long id,
	@Schema(description = "걱정 ID", example = "10") Long worryId,
	@Schema(description = "세션 상태", example = "completed") SessionStatus status,
	@Schema(description = "세션 시작 시각", example = "2026-05-27T22:00:00") LocalDateTime startedAt,
	@Schema(description = "세션 완료 시각", example = "2026-05-27T22:35:00") LocalDateTime completedAt,
	@Schema(description = "세션 전 감정 점수", example = "8") Integer emotionScoreBefore,
	@Schema(description = "세션 후 감정 점수", example = "4") Integer emotionScoreAfter
) {
	public static SessionStatusChangeResponseDTO from(MindSession session) {
		return SessionStatusChangeResponseDTO.builder()
			.id(session.getMindSessionId())
			.worryId(session.getWorryId())
			.status(session.getStatus())
			.startedAt(session.getStartedAt())
			.completedAt(session.getCompletedAt())
			.emotionScoreBefore(session.getEmotionScoreBefore())
			.emotionScoreAfter(session.getEmotionScoreAfter())
			.build();
	}
}

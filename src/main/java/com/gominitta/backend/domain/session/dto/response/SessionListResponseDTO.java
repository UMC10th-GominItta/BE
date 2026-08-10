package com.gominitta.backend.domain.session.dto.response;

import java.time.LocalDateTime;

import com.gominitta.backend.domain.session.entity.MindSession;
import com.gominitta.backend.domain.session.entity.enums.SessionStatus;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;

@Builder
@Schema(description = "세션 목록 항목 응답")
public record SessionListResponseDTO(
	@Schema(description = "세션 ID", example = "1") Long id,
	@Schema(description = "걱정 ID", example = "10") Long worryId,
	@Schema(description = "걱정 제목 (스냅샷)", example = "취업 걱정") String worryTitle,
	@Schema(description = "걱정 내용 (스냅샷)", example = "UMC 프론트가 안 구해지면 어떡하지") String worryContent,
	@Schema(description = "세션 상태", example = "scheduled") SessionStatus status,
	@Schema(description = "세션 예정 시작 시각", example = "2026-05-27T22:00:00") LocalDateTime scheduledStartAt,
	@Schema(description = "세션 예정 종료 시각", example = "2026-05-27T23:00:00") LocalDateTime scheduledEndAt
) {
	public static SessionListResponseDTO from(MindSession session) {
		return SessionListResponseDTO.builder()
			.id(session.getMindSessionId())
			.worryId(session.getWorryId())
			.worryTitle(session.getWorryTitle())
			.worryContent(session.getWorryContent())
			.status(session.getStatus())
			.scheduledStartAt(session.getScheduledStartAt())
			.scheduledEndAt(session.getScheduledEndAt())
			.build();
	}
}

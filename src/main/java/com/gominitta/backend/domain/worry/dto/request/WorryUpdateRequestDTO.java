package com.gominitta.backend.domain.worry.dto.request;


import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;

import java.time.LocalDateTime;

@Builder
@Schema(description = "걱정 수정 요청")
public record WorryUpdateRequestDTO(
        @Schema(description = "걱정 제목", example = "UMC 프론트가 안 구해지면 어쩌지")
        String title,
        @Schema(description = "걱정 내용", example = "걱정걱정걱정")
        String content,
        @Schema(description = "걱정 예약 시작 시각", example = "2026-07-23T21:30:00")
        @JsonProperty("scheduled_start_at")
        LocalDateTime scheduledStartAt,
        @Schema(description = "걱정 예약 종료 시각", example = "2026-07-23T21:30:00")
        @JsonProperty("scheduled_end_at")
        LocalDateTime scheduledEndAt
) {
}

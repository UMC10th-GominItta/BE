package com.gominitta.backend.domain.worry.dto.response;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.gominitta.backend.domain.worry.entity.Worry;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;

import java.time.LocalDateTime;

@Builder
@Schema(description = "걱정 조회 응답")
public record WorryDetailResponseDTO (
        @Schema(description = "걱정 Id")
        Long id,

        @Schema(description = "걱정 제목")
        String title,

        @Schema(description = "걱정 내용")
        String content,

        @Schema(description = "불안도 게이지", example = "8")
        Integer emotionScoreBefore,

        @Schema(description = "걱정 예약 시작 시각", example = "2026-07-23T21:30:00")
        @JsonProperty("scheduled_start_at")
        LocalDateTime scheduledStartAt,

        @Schema(description = "걱정 예약 종료 시각", example = "2026-07-23T21:30:00")
        @JsonProperty("scheduled_end_at")
        LocalDateTime scheduledEndAt
){
    public static WorryDetailResponseDTO from(Worry worry){
        return WorryDetailResponseDTO.builder()
                .id(worry.getId())
                .title(worry.getTitle())
                .content(worry.getContent())
                .emotionScoreBefore(worry.getEmotionScoreBefore())
                .scheduledStartAt(worry.getScheduledStartAt())
                .scheduledEndAt(worry.getScheduledEndAt())
                .build();
    }
}

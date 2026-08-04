package com.gominitta.backend.domain.favortietimeslot.dto.response;


import com.fasterxml.jackson.annotation.JsonProperty;
import com.gominitta.backend.domain.favortietimeslot.entity.FavoriteTime;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;

import java.time.LocalTime;

@Builder
@Schema(description = "즐겨찾는 시간 조회 응답")
public record FavoriteTimeDetailResponseDTO (
        @Schema(description = "즐겨찾는 시간id",example = "1")
        Long id,

        @Schema(description = "즐겨찾는 시간 제목",example = "나는 이시간 좋아")
        String label,

        @Schema(description = "즐겨찾는 시간 시작 시간",example = "09:00:00")
        @JsonProperty("start_time")
        LocalTime startTime,

        @Schema(description = "즐겨찾는 시간 종료 시간",example = "10:00:00")
        @JsonProperty("end_time")
        LocalTime endTime
){
    public static FavoriteTimeDetailResponseDTO from(FavoriteTime favoriteTime){
        return FavoriteTimeDetailResponseDTO.builder()
                .id(favoriteTime.getId())
                .label(favoriteTime.getLabel())
                .startTime(favoriteTime.getStartTime())
                .endTime(favoriteTime.getEndTime())
                .build();


}

        }

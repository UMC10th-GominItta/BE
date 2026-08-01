package com.gominitta.backend.domain.favortietimeslot.dto.request;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import java.time.LocalTime;

@Getter
public class FavoriteTimeRequestDTO {

    @NotBlank
    private String label;

    @Schema(description = "즐겨찾는시간 시작 시간", example = "09:00:00")
    @NotNull
    @JsonProperty("start_time")
    private LocalTime startTime;

    @Schema(description = "즐겨찾는시간 종료 시간", example = "10:00:00")
    @NotNull
    @JsonProperty("end_time")
    private LocalTime endTime;
}

package com.gominitta.backend.domain.session.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import lombok.Builder;

@Builder
@Schema(description = "세션 상태 변경 요청")
public record SessionStatusChangeRequestDTO(
	@Schema(description = "변경할 세션 상태", example = "completed")
	@NotBlank(message = "status는 필수입니다.")
	String status,

	@Schema(description = "세션 후 감정 점수 (0~10, status가 completed일 때 필수)", example = "4")
	@Min(value = 0, message = "감정 점수는 0 이상이어야 합니다.")
	@Max(value = 10, message = "감정 점수는 10 이하여야 합니다.")
	Integer emotionScoreAfter
) {
}

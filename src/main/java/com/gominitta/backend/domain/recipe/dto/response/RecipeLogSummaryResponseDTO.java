package com.gominitta.backend.domain.recipe.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;

@Builder
@Schema(description = "레시피 로그 요약 응답")
public record RecipeLogSummaryResponseDTO(
	@Schema(description = "오늘 완료한 레시피 수", example = "1") long todayCompletedCount,
	@Schema(description = "총 누적 실천 수", example = "3") long totalCompletedCount
) {
}

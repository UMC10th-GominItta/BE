package com.gominitta.backend.domain.recipe.dto.response;

import java.time.LocalDateTime;

import com.gominitta.backend.domain.recipe.entity.RecipeLog;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;

@Builder
@Schema(description = "레시피 로그 응답")
public record RecipeLogResponseDTO(
	@Schema(description = "레시피 로그 ID", example = "101") Long recipeLogId,
	@Schema(description = "레시피 ID", example = "1") Long recipeId,
	@Schema(description = "실행 시각", example = "2026-07-04T14:00:00") LocalDateTime executedAt,
	@Schema(description = "완료 여부", example = "false") Boolean isCompleted
) {
	public static RecipeLogResponseDTO from(RecipeLog log) {
		return RecipeLogResponseDTO.builder()
			.recipeLogId(log.getId())
			.recipeId(log.getRecipeId())
			.executedAt(log.getExecutedAt())
			.isCompleted(log.getIsCompleted())
			.build();
	}
}

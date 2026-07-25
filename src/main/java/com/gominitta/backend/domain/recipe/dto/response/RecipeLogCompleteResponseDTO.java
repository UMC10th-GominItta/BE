package com.gominitta.backend.domain.recipe.dto.response;

import com.gominitta.backend.domain.recipe.entity.RecipeLog;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;

@Builder
@Schema(description = "레시피 완료 응답")
public record RecipeLogCompleteResponseDTO(
	@Schema(description = "레시피 로그 ID", example = "101") Long recipeLogId,
	@Schema(description = "완료 여부", example = "true") Boolean isCompleted
) {
	public static RecipeLogCompleteResponseDTO from(RecipeLog log) {
		return RecipeLogCompleteResponseDTO.builder()
			.recipeLogId(log.getId())
			.isCompleted(log.getIsCompleted())
			.build();
	}
}

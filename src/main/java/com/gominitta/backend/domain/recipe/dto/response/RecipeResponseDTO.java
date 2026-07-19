package com.gominitta.backend.domain.recipe.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;

@Builder
@Schema(description = "레시피 응답")
public record RecipeResponseDTO(
	@Schema(description = "레시피 ID", example = "1") Long recipeId,
	@Schema(description = "레시피 제목", example = "5분 호흡 명상") String title,
	@Schema(description = "레시피 설명", example = "5분간 천천히 호흡하며 마음을 가라앉히는 레시피입니다.") String description,
	@Schema(description = "예상 소요 시간(분)", example = "5") Integer estimatedMinutes
) {
	public static RecipeResponseDTO from(com.gominitta.backend.domain.recipe.entity.Recipe recipe) {
		return RecipeResponseDTO.builder()
			.recipeId(recipe.getId())
			.title(recipe.getTitle())
			.description(recipe.getDescription())
			.estimatedMinutes(recipe.getEstimatedMinutes())
			.build();
	}
}

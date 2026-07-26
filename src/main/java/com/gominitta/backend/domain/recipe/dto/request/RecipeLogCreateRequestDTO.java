package com.gominitta.backend.domain.recipe.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import lombok.Builder;

@Builder
@Schema(description = "레시피 로그 생성 요청")
public record RecipeLogCreateRequestDTO(
	@Schema(description = "실행할 레시피 ID", example = "1")
	@NotNull(message = "레시피 ID는 필수입니다.")
	Long recipeId
) {
}

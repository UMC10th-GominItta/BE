package com.gominitta.backend.domain.recipe.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;
import lombok.Builder;

@Builder
@Schema(description = "레시피 생성 요청")
public record RecipeCreateRequestDTO(
	@Schema(description = "레시피 제목", example = "5분 호흡 명상")
	@NotBlank(message = "제목은 필수입니다.")
	@Size(max = 20, message = "제목은 20자 이하여야 합니다.")
	String title,

	@Schema(description = "레시피 설명", example = "5분간 천천히 호흡하며 마음을 가라앉히는 레시피입니다.")
	@Size(max = 200, message = "설명은 200자 이하여야 합니다.")
	String description,

	@Schema(description = "예상 소요 시간(분)", example = "5")
	@Positive(message = "예상 소요 시간은 1 이상이어야 합니다.")
	Integer estimatedMinutes
) {
}

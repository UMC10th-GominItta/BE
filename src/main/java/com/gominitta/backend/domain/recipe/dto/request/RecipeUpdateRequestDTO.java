package com.gominitta.backend.domain.recipe.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.Size;
import lombok.Builder;

@Builder
@Schema(description = "레시피 수정 요청")
public record RecipeUpdateRequestDTO(
	@Schema(description = "변경할 제목 (미입력 시 변경 없음)", example = "10분 호흡 명상")
	@Size(max = 20, message = "제목은 20자 이하여야 합니다.")
	String title,

	@Schema(description = "변경할 설명 (미입력 시 변경 없음)", example = "10분간 천천히 호흡합니다.")
	@Size(max = 200, message = "설명은 200자 이하여야 합니다.")
	String description,

	@Schema(description = "변경할 예상 소요 시간(분) (미입력 시 변경 없음)", example = "10")
	@Min(value = 1, message = "예상 소요 시간은 1분 이상이어야 합니다.")
	@Max(value = 60, message = "예상 소요 시간은 60분 이하여야 합니다.")
	Integer estimatedMinutes
) {
}

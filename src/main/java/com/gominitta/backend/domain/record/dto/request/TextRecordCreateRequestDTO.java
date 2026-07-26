package com.gominitta.backend.domain.record.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;

@Builder
@Schema(description = "텍스트 기록 생성 요청")
public record TextRecordCreateRequestDTO(
	@Schema(description = "기록할 텍스트 내용", example = "지금 드는 생각을 그대로 적었어요.")
	String contentText
) {
}

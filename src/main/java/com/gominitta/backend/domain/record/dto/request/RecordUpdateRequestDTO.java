package com.gominitta.backend.domain.record.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;

@Builder
@Schema(description = "기록 수정 요청")
public record RecordUpdateRequestDTO(
	@Schema(description = "수정할 텍스트 내용", example = "수정된 기록 내용입니다.")
	String contentText
) {
}

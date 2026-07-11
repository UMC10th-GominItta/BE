package com.gominitta.backend.domain.auth.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import lombok.Builder;

@Builder
@Schema(description = "로그아웃 요청")
public record LogoutRequestDTO(
	@Schema(description = "리프레시 토큰")
	@NotBlank(message = "리프레시 토큰은 필수입니다.")
	String refreshToken
) {
}

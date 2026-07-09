package com.gominitta.backend.domain.auth.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;

@Builder
@Schema(description = "인증 토큰 응답")
public record AuthTokenResponseDTO(
	@Schema(description = "액세스 토큰") String accessToken,
	@Schema(description = "리프레시 토큰") String refreshToken,
	@Schema(description = "신규 유저 여부", example = "false") boolean isNewUser
) {
}

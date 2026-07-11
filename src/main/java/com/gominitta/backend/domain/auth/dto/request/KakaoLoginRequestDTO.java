package com.gominitta.backend.domain.auth.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import lombok.Builder;

@Builder
@Schema(description = "카카오 로그인 요청")
public record KakaoLoginRequestDTO(
	@Schema(description = "카카오 액세스 토큰", example = "kakao_access_token_value")
	@NotBlank(message = "카카오 액세스 토큰은 필수입니다.")
	String kakaoAccessToken
) {
}

package com.gominitta.backend.domain.user.dto.request;

import com.gominitta.backend.domain.user.entity.enums.ProfileIcon;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.Builder;

@Builder
@Schema(description = "유저 프로필 수정 요청")
public record UserUpdateRequestDTO(
	@Schema(description = "변경할 닉네임 (미입력 시 변경 없음)", example = "고민이")
	@Size(max = 10, message = "닉네임은 10자 이하여야 합니다.")
	@Pattern(regexp = "^[가-힣a-zA-Z0-9]*$", message = "닉네임에 특수문자를 사용할 수 없습니다.")
	String nickname,

	@Schema(description = "변경할 프로필 아이콘 (미입력 시 변경 없음)", example = "CHARACTER_1")
	ProfileIcon profileIcon
) {
}

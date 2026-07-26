package com.gominitta.backend.domain.user.dto.response;

import com.gominitta.backend.domain.user.entity.User;
import com.gominitta.backend.domain.user.entity.enums.ProfileIcon;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;

@Builder
@Schema(description = "유저 프로필 응답")
public record UserProfileResponseDTO(
	@Schema(description = "닉네임", example = "고민이") String nickname,
	@Schema(description = "프로필 아이콘") ProfileIcon profileIcon,
	@Schema(description = "이메일", example = "user@example.com") String email
) {
	public static UserProfileResponseDTO from(User user) {
		return UserProfileResponseDTO.builder()
			.nickname(user.getNickname())
			.profileIcon(user.getProfileIcon())
			.email(user.getEmail())
			.build();
	}
}

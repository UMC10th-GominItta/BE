package com.gominitta.backend.domain.user.dto.response;

import com.gominitta.backend.domain.user.entity.User;
import com.gominitta.backend.domain.user.entity.enums.ProfileIcon;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;

@Builder
@Schema(description = "유저 프로필 수정 응답")
public record UserUpdateResponseDTO(
	@Schema(description = "수정된 닉네임", example = "고민이") String nickname,
	@Schema(description = "수정된 프로필 아이콘") ProfileIcon profileIcon
) {
	public static UserUpdateResponseDTO from(User user) {
		return UserUpdateResponseDTO.builder()
			.nickname(user.getNickname())
			.profileIcon(user.getProfileIcon())
			.build();
	}
}

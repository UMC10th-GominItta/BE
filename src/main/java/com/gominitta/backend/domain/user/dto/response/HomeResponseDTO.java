package com.gominitta.backend.domain.user.dto.response;

import java.time.LocalDateTime;

import com.gominitta.backend.domain.dailymessage.entity.DailyMessage;
import com.gominitta.backend.domain.user.entity.User;
import com.gominitta.backend.domain.user.entity.enums.ProfileIcon;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;

@Builder
@Schema(description = "홈 조회 응답")
public record HomeResponseDTO(
	@Schema(description = "유저 정보") UserInfo user,
	@Schema(description = "오늘의 한마디") DailyMessageInfo dailyMessage,
	@Schema(description = "가장 가까운 마음세션 (미구현 시 null)") MindSessionInfo mindSession
) {
	public static HomeResponseDTO of(User user, DailyMessage dailyMessage) {
		return HomeResponseDTO.builder()
			.user(UserInfo.builder()
				.nickname(user.getNickname())
				.profileIcon(user.getProfileIcon())
				.build())
			.dailyMessage(DailyMessageInfo.builder()
				.content(dailyMessage.getContent())
				.build())
			.mindSession(null) // TODO: mind_sessions 도메인 구현 후 실제 세션 데이터로 교체
			.build();
	}

	@Builder
	@Schema(description = "유저 정보")
	public record UserInfo(
		@Schema(description = "닉네임", example = "고민이") String nickname,
		@Schema(description = "프로필 아이콘") ProfileIcon profileIcon
	) {
	}

	@Builder
	@Schema(description = "오늘의 한마디")
	public record DailyMessageInfo(
		@Schema(description = "메시지 내용", example = "오늘 하루도 수고했어요. 당신은 충분히 잘하고 있어요.") String content
	) {
	}

	// TODO: mind_sessions 도메인 구현 후 MindSessionRepository 쿼리 작성 및 매핑 필요
	//  - 현재 시간 기준 가장 가까운 마음세션 1건 조회 후 아래 필드 매핑
	@Builder
	@Schema(description = "마음세션 정보")
	public record MindSessionInfo(
		@Schema(description = "세션 ID", example = "1") Long sessionId,
		@Schema(description = "세션 제목", example = "오늘의 감정 정리") String title,
		@Schema(description = "세션 상태", example = "SCHEDULED") String status,
		@Schema(description = "세션 시작 시각", example = "2025-07-07T14:00:00") LocalDateTime startedAt
	) {
	}
}

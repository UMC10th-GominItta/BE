package com.gominitta.backend.domain.session.controller;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.gominitta.backend.domain.session.dto.response.SessionDetailResponseDTO;
import com.gominitta.backend.domain.session.dto.response.SessionListResponseDTO;
import com.gominitta.backend.domain.session.service.SessionService;
import com.gominitta.backend.global.auth.util.SecurityUtil;
import com.gominitta.backend.global.common.response.ApiResponse;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;

@Tag(name = "Session", description = "마음세션 API")
@RestController
@RequestMapping("/api/v1/sessions")
@RequiredArgsConstructor
public class SessionController {

	private final SessionService sessionService;

	@Operation(
		summary = "세션 목록 조회",
		description = "로그인한 유저의 예정된 세션과 미완료 세션 목록을 시작 시각 순으로 반환합니다. "
			+ "status 파라미터로 scheduled/incomplete 중 하나만 필터링할 수 있습니다.",
		security = @SecurityRequirement(name = "bearerAuth")
	)
	@GetMapping
	public ResponseEntity<ApiResponse<List<SessionListResponseDTO>>> getSessions(
		@RequestParam(required = false) String status
	) {
		Long userId = SecurityUtil.getCurrentUserId();
		List<SessionListResponseDTO> data = sessionService.getSessions(userId, status);
		return ResponseEntity.ok(ApiResponse.success(data));
	}

	@Operation(
		summary = "세션 상세 조회",
		description = "특정 세션의 예약된 걱정 내용과 세션 진행 정보, 세션 기록 목록을 반환합니다.",
		security = @SecurityRequirement(name = "bearerAuth")
	)
	@GetMapping("/{sessionId}")
	public ResponseEntity<ApiResponse<SessionDetailResponseDTO>> getSession(
		@PathVariable Long sessionId
	) {
		Long userId = SecurityUtil.getCurrentUserId();
		SessionDetailResponseDTO data = sessionService.getSessionDetail(userId, sessionId);
		return ResponseEntity.ok(ApiResponse.success(data));
	}
}

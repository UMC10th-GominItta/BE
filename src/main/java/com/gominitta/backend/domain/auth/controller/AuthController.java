package com.gominitta.backend.domain.auth.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.gominitta.backend.domain.auth.dto.request.KakaoLoginRequestDTO;
import com.gominitta.backend.domain.auth.dto.request.LogoutRequestDTO;
import com.gominitta.backend.domain.auth.dto.request.TokenRefreshRequestDTO;
import com.gominitta.backend.domain.auth.dto.response.AuthTokenResponseDTO;
import com.gominitta.backend.domain.auth.service.AuthService;
import com.gominitta.backend.global.common.response.ApiResponse;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@Tag(name = "Auth", description = "인증 API")
@RestController
@RequestMapping("/api/v1/auth")
@RequiredArgsConstructor
public class AuthController {

	private final AuthService authService;

	@Operation(
		summary = "카카오 로그인",
		description = "카카오 액세스 토큰으로 로그인합니다. 신규 유저는 자동으로 회원가입되며, 탈퇴 유저는 재가입 처리됩니다."
	)
	@PostMapping("/kakao")
	public ResponseEntity<ApiResponse<AuthTokenResponseDTO>> kakaoLogin(
		@Valid @RequestBody KakaoLoginRequestDTO request
	) {
		AuthTokenResponseDTO data = authService.kakaoLogin(request);
		return ResponseEntity.ok(ApiResponse.success("로그인에 성공하였습니다.", data));
	}

	@Operation(
		summary = "토큰 재발급",
		description = "리프레시 토큰으로 액세스 토큰과 리프레시 토큰을 재발급합니다."
	)
	@PostMapping("/refresh")
	public ResponseEntity<ApiResponse<AuthTokenResponseDTO>> refresh(
		@Valid @RequestBody TokenRefreshRequestDTO request
	) {
		AuthTokenResponseDTO data = authService.refresh(request);
		return ResponseEntity.ok(ApiResponse.success("토큰 재발급에 성공하였습니다.", data));
	}

	@Operation(
		summary = "로그아웃",
		description = "리프레시 토큰을 무효화합니다.",
		security = @SecurityRequirement(name = "bearerAuth")
	)
	@PostMapping("/logout")
	public ResponseEntity<ApiResponse<Void>> logout(
		@Valid @RequestBody LogoutRequestDTO request
	) {
		authService.logout(request);
		return ResponseEntity.ok(ApiResponse.success("로그아웃에 성공하였습니다.", null));
	}
}

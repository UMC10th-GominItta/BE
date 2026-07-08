package com.gominitta.backend.domain.user.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.gominitta.backend.domain.user.dto.request.UserUpdateRequestDTO;
import com.gominitta.backend.domain.user.dto.response.HomeResponseDTO;
import com.gominitta.backend.domain.user.dto.response.UserProfileResponseDTO;
import com.gominitta.backend.domain.user.dto.response.UserUpdateResponseDTO;
import com.gominitta.backend.domain.user.service.UserService;
import com.gominitta.backend.global.auth.util.SecurityUtil;
import com.gominitta.backend.global.common.response.ApiResponse;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@Tag(name = "User", description = "유저 API")
@RestController
@RequestMapping("/api/v1/users")
@RequiredArgsConstructor
public class UserController {

	private final UserService userService;

	// TODO: JWT 구현 시 활성화 - 현재 JWT 미구현으로 SecurityUtil.getCurrentUserId() 호출 시 UNAUTHORIZED 발생
	@Operation(
		summary = "홈 조회",
		description = "로그인한 유저의 홈 화면 데이터를 조회합니다. 유저 정보, 오늘의 한마디, 가장 가까운 마음세션을 반환합니다.",
		security = @SecurityRequirement(name = "bearerAuth")
	)
	@GetMapping("/home")
	public ResponseEntity<ApiResponse<HomeResponseDTO>> getHome() {
		Long userId = SecurityUtil.getCurrentUserId();
		HomeResponseDTO data = userService.getHome(userId);
		return ResponseEntity.ok(ApiResponse.success("홈 조회에 성공하였습니다.", data));
	}

	// TODO: JWT 구현 시 활성화
	@Operation(
		summary = "내 프로필 조회",
		description = "로그인한 유저의 프로필 정보(닉네임, 프로필 아이콘, 이메일)를 조회합니다.",
		security = @SecurityRequirement(name = "bearerAuth")
	)
	@GetMapping("/me")
	public ResponseEntity<ApiResponse<UserProfileResponseDTO>> getMyProfile() {
		Long userId = SecurityUtil.getCurrentUserId();
		UserProfileResponseDTO data = userService.getMyProfile(userId);
		return ResponseEntity.ok(ApiResponse.success("유저 정보 조회에 성공하였습니다.", data));
	}

	// TODO: JWT 구현 시 활성화
	@Operation(
		summary = "내 프로필 수정",
		description = "닉네임 또는 프로필 아이콘을 수정합니다. 수정할 필드만 포함하면 됩니다. (null 필드는 변경되지 않음)",
		security = @SecurityRequirement(name = "bearerAuth")
	)
	@PatchMapping("/me")
	public ResponseEntity<ApiResponse<UserUpdateResponseDTO>> updateMyProfile(
		@Valid @RequestBody UserUpdateRequestDTO request
	) {
		Long userId = SecurityUtil.getCurrentUserId();
		UserUpdateResponseDTO data = userService.updateMyProfile(userId, request);
		return ResponseEntity.ok(ApiResponse.success("유저 정보 수정에 성공하였습니다.", data));
	}

	// TODO: JWT 구현 시 활성화
	@Operation(
		summary = "회원 탈퇴",
		description = "회원 탈퇴를 처리합니다. 실제 삭제가 아닌 소프트 딜리트(is_deleted=true)로 처리됩니다.",
		security = @SecurityRequirement(name = "bearerAuth")
	)
	@DeleteMapping("/me")
	public ResponseEntity<ApiResponse<Void>> deleteMe() {
		Long userId = SecurityUtil.getCurrentUserId();
		userService.deleteMe(userId);
		return ResponseEntity.ok(ApiResponse.success("회원 탈퇴에 성공하였습니다.", null));
	}
}

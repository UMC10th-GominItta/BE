package com.gominitta.backend.domain.recipe.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.gominitta.backend.domain.recipe.dto.request.RecipeLogCreateRequestDTO;
import com.gominitta.backend.domain.recipe.dto.response.RecipeLogCompleteResponseDTO;
import com.gominitta.backend.domain.recipe.dto.response.RecipeLogResponseDTO;
import com.gominitta.backend.domain.recipe.dto.response.RecipeLogSummaryResponseDTO;
import com.gominitta.backend.domain.recipe.service.RecipeLogService;
import com.gominitta.backend.global.auth.util.SecurityUtil;
import com.gominitta.backend.global.common.response.ApiResponse;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@Tag(name = "RecipeLog", description = "레시피 로그 API")
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/recipe-logs")
public class RecipeLogController {

	private final RecipeLogService recipeLogService;

	@Operation(
		summary = "레시피 실행 기록 생성",
		description = "레시피 실행을 시작할 때 로그를 생성합니다.",
		security = @SecurityRequirement(name = "bearerAuth")
	)
	@PostMapping
	public ResponseEntity<ApiResponse<RecipeLogResponseDTO>> createRecipeLog(
		@Valid @RequestBody RecipeLogCreateRequestDTO request
	) {
		Long userId = SecurityUtil.getCurrentUserId();
		RecipeLogResponseDTO data = recipeLogService.createRecipeLog(userId, request);
		return ResponseEntity.status(HttpStatus.CREATED)
			.body(ApiResponse.success("201", "레시피 실행이 시작되었습니다.", data));
	}

	@Operation(
		summary = "레시피 완료 처리",
		description = "레시피 실행을 완료 처리합니다.",
		security = @SecurityRequirement(name = "bearerAuth")
	)
	@PatchMapping("/{recipeLogId}")
	public ResponseEntity<ApiResponse<RecipeLogCompleteResponseDTO>> completeRecipeLog(
		@PathVariable Long recipeLogId
	) {
		Long userId = SecurityUtil.getCurrentUserId();
		RecipeLogCompleteResponseDTO data = recipeLogService.completeRecipeLog(userId, recipeLogId);
		return ResponseEntity.ok(ApiResponse.success("레시피 실행이 완료되었습니다.", data));
	}

	@Operation(
		summary = "완료 레시피 집계 조회",
		description = "오늘 완료한 레시피 수와 총 누적 실천 수를 반환합니다.",
		security = @SecurityRequirement(name = "bearerAuth")
	)
	@GetMapping("/summary")
	public ResponseEntity<ApiResponse<RecipeLogSummaryResponseDTO>> getSummary() {
		Long userId = SecurityUtil.getCurrentUserId();
		RecipeLogSummaryResponseDTO data = recipeLogService.getSummary(userId);
		return ResponseEntity.ok(ApiResponse.success("요청이 성공했습니다.", data));
	}
}

package com.gominitta.backend.domain.recipe.controller;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.gominitta.backend.domain.recipe.dto.request.RecipeCreateRequestDTO;
import com.gominitta.backend.domain.recipe.dto.request.RecipeUpdateRequestDTO;
import com.gominitta.backend.domain.recipe.dto.response.RecipeResponseDTO;
import com.gominitta.backend.domain.recipe.service.RecipeService;
import com.gominitta.backend.global.auth.util.SecurityUtil;
import com.gominitta.backend.global.common.response.ApiResponse;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@Tag(name = "Recipe", description = "레시피 API")
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/recipes")
public class RecipeController {

	private final RecipeService recipeService;

	@Operation(
		summary = "랜덤 시스템 레시피 조회",
		description = "시스템 레시피 중 3개를 랜덤으로 반환합니다.",
		security = @SecurityRequirement(name = "bearerAuth")
	)
	@GetMapping("/random")
	public ResponseEntity<ApiResponse<List<RecipeResponseDTO>>> getRandomRecipes() {
		SecurityUtil.getCurrentUserId();
		List<RecipeResponseDTO> data = recipeService.getRandomSystemRecipes();
		return ResponseEntity.ok(ApiResponse.success("요청이 성공했습니다.", data));
	}

	@Operation(
		summary = "레시피 목록 조회",
		description = "scope=mine이면 내 레시피, scope=system이면 시스템 레시피 목록을 반환합니다.",
		security = @SecurityRequirement(name = "bearerAuth")
	)
	@GetMapping
	public ResponseEntity<ApiResponse<List<RecipeResponseDTO>>> getRecipes(
		@RequestParam(defaultValue = "mine") String scope
	) {
		Long userId = SecurityUtil.getCurrentUserId();
		List<RecipeResponseDTO> data = recipeService.getRecipes(userId, scope);
		return ResponseEntity.ok(ApiResponse.success("요청이 성공했습니다.", data));
	}

	@Operation(
		summary = "레시피 상세 조회",
		description = "레시피 ID로 단건 조회합니다.",
		security = @SecurityRequirement(name = "bearerAuth")
	)
	@GetMapping("/{recipeId}")
	public ResponseEntity<ApiResponse<RecipeResponseDTO>> getRecipe(
		@PathVariable Long recipeId
	) {
		SecurityUtil.getCurrentUserId();
		RecipeResponseDTO data = recipeService.getRecipe(recipeId);
		return ResponseEntity.ok(ApiResponse.success("요청이 성공했습니다.", data));
	}

	@Operation(
		summary = "레시피 생성",
		description = "새 레시피를 생성합니다.",
		security = @SecurityRequirement(name = "bearerAuth")
	)
	@PostMapping
	public ResponseEntity<ApiResponse<RecipeResponseDTO>> createRecipe(
		@Valid @RequestBody RecipeCreateRequestDTO request
	) {
		Long userId = SecurityUtil.getCurrentUserId();
		RecipeResponseDTO data = recipeService.createRecipe(userId, request);
		return ResponseEntity.status(HttpStatus.CREATED)
			.body(ApiResponse.success("201", "레시피가 등록되었습니다.", data));
	}

	@Operation(
		summary = "레시피 수정",
		description = "레시피 정보를 수정합니다. 수정할 필드만 포함하면 됩니다. (null 필드는 변경되지 않음)",
		security = @SecurityRequirement(name = "bearerAuth")
	)
	@PatchMapping("/{recipeId}")
	public ResponseEntity<ApiResponse<RecipeResponseDTO>> updateRecipe(
		@PathVariable Long recipeId,
		@Valid @RequestBody RecipeUpdateRequestDTO request
	) {
		Long userId = SecurityUtil.getCurrentUserId();
		RecipeResponseDTO data = recipeService.updateRecipe(userId, recipeId, request);
		return ResponseEntity.ok(ApiResponse.success("레시피가 수정되었습니다.", data));
	}

	@Operation(
		summary = "레시피 삭제",
		description = "레시피를 삭제합니다.",
		security = @SecurityRequirement(name = "bearerAuth")
	)
	@DeleteMapping("/{recipeId}")
	public ResponseEntity<ApiResponse<Void>> deleteRecipe(
		@PathVariable Long recipeId
	) {
		Long userId = SecurityUtil.getCurrentUserId();
		recipeService.deleteRecipe(userId, recipeId);
		return ResponseEntity.ok(ApiResponse.success("레시피가 삭제되었습니다.", null));
	}
}

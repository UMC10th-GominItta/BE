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
import com.gominitta.backend.global.auth.util.SecurityUtil;
import com.gominitta.backend.global.common.response.ApiResponse;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;

@Tag(name = "Recipe", description = "레시피 API")
@RestController
@RequestMapping("/api/v1/recipes")
public class RecipeController {

	@Operation(
		summary = "레시피 목록 조회",
		description = "scope=mine이면 내 레시피, scope=system이면 시스템 레시피 목록을 반환합니다.",
		security = @SecurityRequirement(name = "bearerAuth")
	)
	@GetMapping
	public ResponseEntity<ApiResponse<List<RecipeResponseDTO>>> getRecipes(
		@RequestParam(defaultValue = "mine") String scope
	) {
		SecurityUtil.getCurrentUserId();
		List<RecipeResponseDTO> data = List.of(
			RecipeResponseDTO.builder()
				.recipeId(1L)
				.title("5분 호흡 명상")
				.description("5분간 천천히 호흡하며 마음을 가라앉히는 레시피입니다.")
				.estimatedMinutes(5)
				.build()
		);
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
		RecipeResponseDTO data = RecipeResponseDTO.builder()
			.recipeId(recipeId)
			.title("5분 호흡 명상")
			.description("5분간 천천히 호흡하며 마음을 가라앉히는 레시피입니다.")
			.estimatedMinutes(5)
			.build();
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
		SecurityUtil.getCurrentUserId();
		RecipeResponseDTO data = RecipeResponseDTO.builder()
			.recipeId(1L)
			.title(request.title())
			.description(request.description())
			.estimatedMinutes(request.estimatedMinutes())
			.build();
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
		SecurityUtil.getCurrentUserId();
		RecipeResponseDTO data = RecipeResponseDTO.builder()
			.recipeId(recipeId)
			.title(request.title())
			.description(request.description())
			.estimatedMinutes(request.estimatedMinutes())
			.build();
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
		SecurityUtil.getCurrentUserId();
		return ResponseEntity.ok(ApiResponse.success("레시피가 삭제되었습니다.", null));
	}
}

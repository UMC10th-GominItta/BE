package com.gominitta.backend.domain.record.controller;

import java.util.List;

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

import com.gominitta.backend.domain.record.dto.request.RecordUpdateRequestDTO;
import com.gominitta.backend.domain.record.dto.request.TextRecordCreateRequestDTO;
import com.gominitta.backend.domain.record.dto.response.SessionRecordResponseDTO;
import com.gominitta.backend.domain.record.service.RecordService;
import com.gominitta.backend.global.auth.util.SecurityUtil;
import com.gominitta.backend.global.common.response.ApiResponse;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;

@Tag(name = "Record", description = "세션 기록 API")
@RestController
@RequestMapping("/api/v1/sessions/{id}/records")
@RequiredArgsConstructor
public class RecordController {

	private final RecordService recordService;

	@Operation(
		summary = "세션 기록 목록 조회",
		description = "특정 세션에 작성된 기록(텍스트/음성/필기) 목록을 반환합니다. "
			+ "recordType 파라미터로 text/voice/handwriting 중 하나만 필터링할 수 있습니다.",
		security = @SecurityRequirement(name = "bearerAuth")
	)
	@GetMapping
	public ResponseEntity<ApiResponse<List<SessionRecordResponseDTO>>> getSessionRecords(
		@PathVariable Long id,
		@RequestParam(required = false) String recordType
	) {
		Long userId = SecurityUtil.getCurrentUserId();
		List<SessionRecordResponseDTO> data = recordService.getSessionRecords(userId, id, recordType);
		return ResponseEntity.ok(ApiResponse.success(data));
	}

	@Operation(
		summary = "텍스트 기록 생성",
		description = "세션 진행 중 떠오른 생각을 텍스트로 기록합니다.",
		security = @SecurityRequirement(name = "bearerAuth")
	)
	@PostMapping
	public ResponseEntity<ApiResponse<SessionRecordResponseDTO>> createTextRecord(
		@PathVariable Long id,
		@RequestBody TextRecordCreateRequestDTO request
	) {
		Long userId = SecurityUtil.getCurrentUserId();
		SessionRecordResponseDTO data = recordService.createTextRecord(userId, id, request);
		return ResponseEntity.ok(ApiResponse.success(data));
	}

	@Operation(
		summary = "기록 수정",
		description = "이미 작성한 기록의 텍스트 내용을 수정합니다.",
		security = @SecurityRequirement(name = "bearerAuth")
	)
	@PatchMapping("/{recordId}")
	public ResponseEntity<ApiResponse<SessionRecordResponseDTO>> updateRecord(
		@PathVariable Long id,
		@PathVariable Long recordId,
		@RequestBody RecordUpdateRequestDTO request
	) {
		Long userId = SecurityUtil.getCurrentUserId();
		SessionRecordResponseDTO data = recordService.updateRecord(userId, id, recordId, request);
		return ResponseEntity.ok(ApiResponse.success(data));
	}

	@Operation(
		summary = "기록 삭제",
		description = "작성한 기록을 삭제합니다.",
		security = @SecurityRequirement(name = "bearerAuth")
	)
	@DeleteMapping("/{recordId}")
	public ResponseEntity<ApiResponse<Void>> deleteRecord(
		@PathVariable Long id,
		@PathVariable Long recordId
	) {
		Long userId = SecurityUtil.getCurrentUserId();
		recordService.deleteRecord(userId, id, recordId);
		return ResponseEntity.ok(ApiResponse.success(null));
	}
}

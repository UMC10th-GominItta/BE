package com.gominitta.backend.domain.worry.controller;

import com.gominitta.backend.domain.worry.dto.request.WorryCreateRequestDTO;
import com.gominitta.backend.domain.worry.dto.request.WorryUpdateRequestDTO;
import com.gominitta.backend.domain.worry.dto.response.WorryDetailResponseDTO;
import com.gominitta.backend.domain.worry.dto.response.WorryResponseDTO;
import com.gominitta.backend.domain.worry.service.WorryService;
import com.gominitta.backend.global.common.response.ApiResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/worries")
@RequiredArgsConstructor
public class WorryController {

    private final WorryService worryService;

    @PostMapping
    public ResponseEntity<ApiResponse<WorryResponseDTO>> create(@Valid @RequestBody WorryCreateRequestDTO request) {
        return ResponseEntity.ok(ApiResponse.success(worryService.createWorry(request)));
    }

    @GetMapping
    public ResponseEntity<ApiResponse<List<WorryDetailResponseDTO>>> getWorries() {
        return ResponseEntity.ok(ApiResponse.success(worryService.getWorries()));
    }

    @GetMapping("/{worryId}")
    public ResponseEntity<ApiResponse<WorryDetailResponseDTO>> getWorry(@PathVariable Long worryId) {
        return ResponseEntity.ok(ApiResponse.success(worryService.getWorry(worryId)));
    }

    @PatchMapping("/{worryId}")
    public ResponseEntity<ApiResponse<WorryResponseDTO>> updateWorry(@PathVariable Long worryId, @Valid @RequestBody WorryUpdateRequestDTO request) {
        return ResponseEntity.ok(ApiResponse.success(worryService.updateWorry(worryId, request)));
    }

    @DeleteMapping("/{worryId}")
    public ResponseEntity<ApiResponse<WorryResponseDTO>> deleteWorry(@PathVariable Long worryId) {
        return ResponseEntity.ok(ApiResponse.success(worryService.deleteWorry(worryId)));
    }
}

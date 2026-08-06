package com.gominitta.backend.domain.favortietimeslot.controller;


import com.gominitta.backend.domain.favortietimeslot.dto.request.FavoriteTimeRequestDTO;
import com.gominitta.backend.domain.favortietimeslot.dto.response.FavoriteTimeDetailResponseDTO;
import com.gominitta.backend.domain.favortietimeslot.dto.response.FavoriteTimeResponseDTO;
import com.gominitta.backend.domain.favortietimeslot.service.FavoriteTimeService;
import com.gominitta.backend.global.common.response.ApiResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/favoriteTimeSlots")
@RequiredArgsConstructor
public class FavoriteTimeController {
    private final FavoriteTimeService favoriteTimeService;

    @PostMapping
    public ResponseEntity<ApiResponse<FavoriteTimeResponseDTO>> create(@Valid @RequestBody FavoriteTimeRequestDTO request) {
        return ResponseEntity.ok(ApiResponse.success(favoriteTimeService.createFavoriteTime(request)));
    }

    @GetMapping
    public ResponseEntity<ApiResponse<List<FavoriteTimeDetailResponseDTO>>> getFavoriteTimes() {
        return ResponseEntity.ok(ApiResponse.success(favoriteTimeService.getFavoriteTimes()));
    }

    @PatchMapping("/{favoriteTimeSlotsId}")
    public ResponseEntity<ApiResponse<FavoriteTimeResponseDTO>> updateFavoriteTime(@PathVariable Long favoriteTimeSlotsId, @Valid @RequestBody FavoriteTimeRequestDTO request) {
        return ResponseEntity.ok(ApiResponse.success(favoriteTimeService.updateFavoriteTime(favoriteTimeSlotsId, request)));
    }

    @GetMapping("/{favoriteTimeSlotsId}")
    public ResponseEntity<ApiResponse<FavoriteTimeDetailResponseDTO>> getFavoriteTime(@PathVariable Long favoriteTimeSlotsId) {
        return ResponseEntity.ok(ApiResponse.success(favoriteTimeService.getFavoriteTime(favoriteTimeSlotsId)));
    }

    @DeleteMapping("/{favoriteTimeSlotsId}")
    public ResponseEntity<ApiResponse<FavoriteTimeResponseDTO>> deleteFavoriteTime(@PathVariable Long favoriteTimeSlotsId) {
        return ResponseEntity.ok(ApiResponse.success(favoriteTimeService.deleteFavoriteTime(favoriteTimeSlotsId)));
    }

}

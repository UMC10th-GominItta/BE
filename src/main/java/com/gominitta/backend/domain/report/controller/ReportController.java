package com.gominitta.backend.domain.report.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.gominitta.backend.domain.notification.controller.CurrentUserProvider;
import com.gominitta.backend.domain.report.dto.response.WorryThemeResponseDTO;
import com.gominitta.backend.domain.report.enums.PeriodType;
import com.gominitta.backend.domain.report.service.ReportService;
import com.gominitta.backend.global.common.response.ApiResponse;

import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/reports")
public class ReportController {

	private final ReportService reportService;

	@GetMapping("/worry-themes")
	public ApiResponse<WorryThemeResponseDTO> getWorryThemes(
		@RequestParam(defaultValue = "30d") String period) {

		Long userId = CurrentUserProvider.getCurrentUserId();
		PeriodType periodType = PeriodType.from(period);

		return ApiResponse.success(reportService.getWorryThemes(userId, periodType));
	}
}

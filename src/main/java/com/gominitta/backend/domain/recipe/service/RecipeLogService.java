package com.gominitta.backend.domain.recipe.service;

import java.time.LocalDate;
import java.time.LocalDateTime;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.gominitta.backend.domain.recipe.dto.request.RecipeLogCreateRequestDTO;
import com.gominitta.backend.domain.recipe.dto.response.RecipeLogCompleteResponseDTO;
import com.gominitta.backend.domain.recipe.dto.response.RecipeLogResponseDTO;
import com.gominitta.backend.domain.recipe.dto.response.RecipeLogSummaryResponseDTO;
import com.gominitta.backend.domain.recipe.entity.RecipeLog;
import com.gominitta.backend.domain.recipe.exception.RecipeErrorCode;
import com.gominitta.backend.domain.recipe.repository.RecipeLogRepository;
import com.gominitta.backend.domain.recipe.repository.RecipeRepository;
import com.gominitta.backend.global.common.exception.GeneralException;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class RecipeLogService {

	private final RecipeLogRepository recipeLogRepository;
	private final RecipeRepository recipeRepository;

	@Transactional
	public RecipeLogResponseDTO createRecipeLog(Long userId, RecipeLogCreateRequestDTO request) {
		recipeRepository.findByIdAndIsDeletedFalse(request.recipeId())
			.orElseThrow(() -> new GeneralException(RecipeErrorCode.RECIPE_NOT_FOUND));

		RecipeLog log = RecipeLog.create(
			userId,
			request.recipeId(),
			LocalDateTime.now()
		);
		recipeLogRepository.save(log);
		return RecipeLogResponseDTO.from(log);
	}

	@Transactional
	public RecipeLogCompleteResponseDTO completeRecipeLog(Long userId, Long recipeLogId) {
		RecipeLog log = recipeLogRepository.findByIdAndIsDeletedFalse(recipeLogId)
			.orElseThrow(() -> new GeneralException(RecipeErrorCode.RECIPE_LOG_NOT_FOUND));

		if (!log.getUserId().equals(userId)) {
			throw new GeneralException(RecipeErrorCode.RECIPE_LOG_FORBIDDEN);
		}
		if (log.getIsCompleted()) {
			throw new GeneralException(RecipeErrorCode.RECIPE_LOG_ALREADY_COMPLETED);
		}

		log.complete();
		return RecipeLogCompleteResponseDTO.from(log);
	}

	@Transactional(readOnly = true)
	public RecipeLogSummaryResponseDTO getSummary(Long userId) {
		LocalDateTime startOfDay = LocalDate.now().atStartOfDay();
		LocalDateTime endOfDay = startOfDay.plusDays(1);

		long todayCount = recipeLogRepository
			.countByUserIdAndIsCompletedTrueAndIsDeletedFalseAndExecutedAtBetween(userId, startOfDay, endOfDay);
		long totalCount = recipeLogRepository
			.countByUserIdAndIsCompletedTrueAndIsDeletedFalse(userId);

		return RecipeLogSummaryResponseDTO.builder()
			.todayCompletedCount(todayCount)
			.totalCompletedCount(totalCount)
			.build();
	}
}

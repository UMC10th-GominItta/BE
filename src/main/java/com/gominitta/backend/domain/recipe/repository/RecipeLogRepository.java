package com.gominitta.backend.domain.recipe.repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.gominitta.backend.domain.recipe.entity.RecipeLog;

public interface RecipeLogRepository extends JpaRepository<RecipeLog, Long> {

	Optional<RecipeLog> findByIdAndIsDeletedFalse(Long id);

	List<RecipeLog> findByUserIdAndIsDeletedFalse(Long userId);

	List<RecipeLog> findByRecipeIdAndIsDeletedFalse(Long recipeId);

	long countByUserIdAndIsCompletedTrueAndIsDeletedFalse(Long userId);

	long countByUserIdAndIsCompletedTrueAndIsDeletedFalseAndExecutedAtBetween(
		Long userId, LocalDateTime start, LocalDateTime end);
}

package com.gominitta.backend.domain.recipe.service;

import java.util.Collections;
import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.gominitta.backend.domain.recipe.dto.request.RecipeCreateRequestDTO;
import com.gominitta.backend.domain.recipe.dto.request.RecipeUpdateRequestDTO;
import com.gominitta.backend.domain.recipe.dto.response.RecipeResponseDTO;
import com.gominitta.backend.domain.recipe.entity.Recipe;
import com.gominitta.backend.domain.recipe.exception.RecipeErrorCode;
import com.gominitta.backend.domain.recipe.repository.RecipeRepository;
import com.gominitta.backend.global.common.exception.GeneralException;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class RecipeService {

	private final RecipeRepository recipeRepository;

	@Transactional(readOnly = true)
	public List<RecipeResponseDTO> getRecipes(Long userId, String scope) {
		List<Recipe> recipes = switch (scope) {
			case "system" -> recipeRepository.findByUserIdIsNullAndIsDeletedFalse();
			default -> recipeRepository.findByUserIdAndIsDeletedFalse(userId);
		};
		return recipes.stream()
			.map(RecipeResponseDTO::from)
			.toList();
	}

	@Transactional(readOnly = true)
	public RecipeResponseDTO getRecipe(Long userId, Long recipeId) {
		Recipe recipe = findActiveRecipe(recipeId);

		if (recipe.getUserId() != null && !recipe.getUserId().equals(userId)) {
			throw new GeneralException(RecipeErrorCode.RECIPE_VIEW_FORBIDDEN);
		}

		return RecipeResponseDTO.from(recipe);
	}

	@Transactional
	public RecipeResponseDTO createRecipe(Long userId, RecipeCreateRequestDTO request) {
		Recipe recipe = Recipe.create(userId, request.title(), request.description(), request.estimatedMinutes());
		recipeRepository.save(recipe);
		return RecipeResponseDTO.from(recipe);
	}

	@Transactional
	public RecipeResponseDTO updateRecipe(Long userId, Long recipeId, RecipeUpdateRequestDTO request) {
		Recipe recipe = findActiveRecipe(recipeId);

		if (!recipe.getUserId().equals(userId)) {
			throw new GeneralException(RecipeErrorCode.RECIPE_FORBIDDEN);
		}

		if (request.title() != null) {
			recipe.updateTitle(request.title());
		}
		if (request.description() != null) {
			recipe.updateDescription(request.description());
		}
		if (request.estimatedMinutes() != null) {
			recipe.updateEstimatedMinutes(request.estimatedMinutes());
		}

		return RecipeResponseDTO.from(recipe);
	}

	@Transactional
	public void deleteRecipe(Long userId, Long recipeId) {
		Recipe recipe = findActiveRecipe(recipeId);

		if (!recipe.getUserId().equals(userId)) {
			throw new GeneralException(RecipeErrorCode.RECIPE_DELETE_FORBIDDEN);
		}

		recipe.delete();
	}

	@Transactional(readOnly = true)
	public List<RecipeResponseDTO> getRandomSystemRecipes() {
		List<Recipe> recipes = new java.util.ArrayList<>(recipeRepository.findByUserIdIsNullAndIsDeletedFalse());
		Collections.shuffle(recipes);
		return recipes.stream()
			.limit(3)
			.map(RecipeResponseDTO::from)
			.toList();
	}

	private Recipe findActiveRecipe(Long recipeId) {
		return recipeRepository.findByIdAndIsDeletedFalse(recipeId)
			.orElseThrow(() -> new GeneralException(RecipeErrorCode.RECIPE_NOT_FOUND));
	}
}

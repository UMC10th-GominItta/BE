package com.gominitta.backend.domain.recipe.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.gominitta.backend.domain.recipe.entity.Recipe;

public interface RecipeRepository extends JpaRepository<Recipe, Long> {

	Optional<Recipe> findByIdAndIsDeletedFalse(Long id);

	List<Recipe> findByUserIdAndIsDeletedFalse(Long userId);

	List<Recipe> findByUserIdIsNullAndIsDeletedFalse();
}

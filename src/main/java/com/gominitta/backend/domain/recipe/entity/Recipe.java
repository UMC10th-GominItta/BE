package com.gominitta.backend.domain.recipe.entity;

import com.gominitta.backend.global.common.entity.BaseEntity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Entity
@Table(name = "recipes")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Recipe extends BaseEntity {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "recipe_id")
	private Long id;

	/* null이면 시스템 레시피 */
	@Column(name = "user_id")
	private Long userId;

	@Column(name = "title", nullable = false, length = 20)
	private String title;

	@Column(name = "description", length = 200)
	private String description;

	@Column(name = "estimated_minutes")
	private Integer estimatedMinutes;

	public static Recipe create(Long userId, String title, String description, Integer estimatedMinutes) {
		Recipe recipe = new Recipe();
		recipe.userId = userId;
		recipe.title = title;
		recipe.description = description;
		recipe.estimatedMinutes = estimatedMinutes;
		return recipe;
	}

	public void updateTitle(String title) {
		this.title = title;
	}

	public void updateDescription(String description) {
		this.description = description;
	}

	public void updateEstimatedMinutes(Integer estimatedMinutes) {
		this.estimatedMinutes = estimatedMinutes;
	}

	public void delete() {
		softDelete();
	}
}

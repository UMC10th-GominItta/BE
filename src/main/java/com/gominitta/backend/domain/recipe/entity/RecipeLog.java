package com.gominitta.backend.domain.recipe.entity;

import java.time.LocalDateTime;

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
@Table(name = "recipe_logs")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class RecipeLog extends BaseEntity {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "recipe_logs_id")
	private Long id;

	@Column(name = "user_id", nullable = false)
	private Long userId;

	@Column(name = "recipe_id", nullable = false)
	private Long recipeId;

	@Column(name = "mind_sessions_id")
	private Long mindSessionsId;

	@Column(name = "executed_at")
	private LocalDateTime executedAt;

	@Column(name = "is_completed")
	private Boolean isCompleted;

	public static RecipeLog create(Long userId, Long recipeId, Long mindSessionsId, LocalDateTime executedAt) {
		RecipeLog log = new RecipeLog();
		log.userId = userId;
		log.recipeId = recipeId;
		log.mindSessionsId = mindSessionsId;
		log.executedAt = executedAt;
		log.isCompleted = false;
		return log;
	}

	public void complete() {
		this.isCompleted = true;
	}
}

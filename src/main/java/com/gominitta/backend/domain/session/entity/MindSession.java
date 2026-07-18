package com.gominitta.backend.domain.session.entity;

import java.time.LocalDateTime;

import com.gominitta.backend.domain.session.entity.enums.SessionStatus;
import com.gominitta.backend.domain.session.entity.enums.ThemeCategory;
import com.gominitta.backend.global.common.entity.BaseEntity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Entity
@Table(name = "mind_sessions")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class MindSession extends BaseEntity {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "id")
	private Long mindSessionId;

	@Column(name = "user_id", nullable = false)
	private Long userId;

	@Column(name = "worry_id", nullable = false, unique = true)
	private Long worryId;

	@Enumerated(EnumType.STRING)
	@Column(name = "status", nullable = false, length = 10)
	private SessionStatus status;

	@Column(name = "worry_content", nullable = false, columnDefinition = "TEXT")
	private String worryContent;

	@Enumerated(EnumType.STRING)
	@Column(name = "theme_category", length = 10)
	private ThemeCategory themeCategory;

	@Column(name = "emotion_score_before")
	private Integer emotionScoreBefore;

	@Column(name = "scheduled_start_at", nullable = false)
	private LocalDateTime scheduledStartAt;

	@Column(name = "scheduled_end_at", nullable = false)
	private LocalDateTime scheduledEndAt;

	@Column(name = "started_at")
	private LocalDateTime startedAt;

	@Column(name = "completed_at")
	private LocalDateTime completedAt;

	@Column(name = "emotion_score_after")
	private Integer emotionScoreAfter;

	public static MindSession create(Long userId, Long worryId, String worryContent, ThemeCategory themeCategory,
			Integer emotionScoreBefore, LocalDateTime scheduledStartAt, LocalDateTime scheduledEndAt) {
		MindSession session = new MindSession();
		session.userId = userId;
		session.worryId = worryId;
		session.status = SessionStatus.SCHEDULED;
		session.worryContent = worryContent;
		session.themeCategory = themeCategory;
		session.emotionScoreBefore = emotionScoreBefore;
		session.scheduledStartAt = scheduledStartAt;
		session.scheduledEndAt = scheduledEndAt;
		return session;
	}

	public void start() {
		if (this.status == SessionStatus.COMPLETED) {
			throw new IllegalStateException("이미 완료된 세션은 시작할 수 없습니다.");
		}
		this.status = SessionStatus.IN_PROGRESS;
		this.startedAt = LocalDateTime.now();
	}

	public void complete(Integer emotionScoreAfter) {
		if (this.status == SessionStatus.COMPLETED) {
			throw new IllegalStateException("이미 완료된 세션입니다.");
		}
		this.status = SessionStatus.COMPLETED;
		this.completedAt = LocalDateTime.now();
		this.emotionScoreAfter = emotionScoreAfter;
	}
}

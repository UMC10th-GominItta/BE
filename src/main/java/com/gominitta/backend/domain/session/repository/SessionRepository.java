package com.gominitta.backend.domain.session.repository;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.gominitta.backend.domain.session.entity.MindSession;
import com.gominitta.backend.domain.session.entity.enums.SessionStatus;
import com.gominitta.backend.domain.session.entity.enums.ThemeCategory;

public interface SessionRepository extends JpaRepository<MindSession, Long> {

	List<MindSession> findByUserIdAndStatusInOrderByScheduledStartAtAsc(Long userId, List<SessionStatus> statuses);

	boolean existsByWorryId(Long worryId);

	// 세션 시작 알림 대상: 시작 예정 시각이 범위 안이고, 아직 SCHEDULED 상태인 세션
	List<MindSession> findByScheduledStartAtBetweenAndStatus(
		LocalDateTime start, LocalDateTime end, SessionStatus status);

	@Query("SELECT s.themeCategory AS themeCategory, COUNT(s) AS count "
		+ "FROM MindSession s "
		+ "WHERE s.userId = :userId "
		+ "AND s.themeCategory IS NOT NULL "
		+ "AND s.isDeleted = false "
		+ "AND s.createdAt >= :from "
		+ "GROUP BY s.themeCategory")
	List<ThemeCategoryCount> findThemeCategoryCounts(@Param("userId") Long userId,
		@Param("from") LocalDateTime from);

	interface ThemeCategoryCount {
		ThemeCategory getThemeCategory();

		long getCount();
	}

	@Query("SELECT AVG(s.emotionScoreBefore) AS avgBefore, "
		+ "AVG(s.emotionScoreAfter) AS avgAfter, "
		+ "COUNT(s) AS sessionCount "
		+ "FROM MindSession s "
		+ "WHERE s.userId = :userId "
		+ "AND s.status = com.gominitta.backend.domain.session.entity.enums.SessionStatus.COMPLETED "
		+ "AND s.isDeleted = false "
		+ "AND s.completedAt >= :from")
	AnxietyGapAggregate findAnxietyGap(@Param("userId") Long userId, @Param("from") LocalDateTime from);

	interface AnxietyGapAggregate {
		Double getAvgBefore();

		Double getAvgAfter();

		long getSessionCount();
	}

	// 기간 내 모든 세션의 걱정 작성 시각 추출 (타임라인 집계 기준: 걱정을 예약한 시각 = createdAt)
	@Query("SELECT s.createdAt "
		+ "FROM MindSession s "
		+ "WHERE s.userId = :userId "
		+ "AND s.isDeleted = false "
		+ "AND s.createdAt >= :from")
	List<LocalDateTime> findCreatedAtsForTimeline(@Param("userId") Long userId,
		@Param("from") LocalDateTime from);
}

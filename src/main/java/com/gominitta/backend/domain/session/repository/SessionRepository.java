package com.gominitta.backend.domain.session.repository;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.gominitta.backend.domain.session.entity.MindSession;
import com.gominitta.backend.domain.session.entity.enums.SessionStatus;

public interface SessionRepository extends JpaRepository<MindSession, Long> {

	List<MindSession> findByUserIdAndStatusInOrderByScheduledStartAtAsc(Long userId, List<SessionStatus> statuses);

	boolean existsByWorryId(Long worryId);

	// 세션 시작 알림 대상: 시작 예정 시각이 범위 안이고, 아직 SCHEDULED 상태인 세션
	List<MindSession> findByScheduledStartAtBetweenAndStatus(
		LocalDateTime start, LocalDateTime end, SessionStatus status);
}

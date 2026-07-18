package com.gominitta.backend.domain.session.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.gominitta.backend.domain.session.entity.MindSession;
import com.gominitta.backend.domain.session.entity.enums.SessionStatus;

public interface SessionRepository extends JpaRepository<MindSession, Long> {

	List<MindSession> findByUserIdAndStatusInOrderByScheduledStartAtAsc(Long userId, List<SessionStatus> statuses);
}

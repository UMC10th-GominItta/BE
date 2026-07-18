package com.gominitta.backend.domain.session.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.gominitta.backend.domain.session.entity.SessionRecord;

public interface SessionRecordRepository extends JpaRepository<SessionRecord, Long> {

	List<SessionRecord> findBySessionIdOrderByCreatedAtAsc(Long sessionId);
}

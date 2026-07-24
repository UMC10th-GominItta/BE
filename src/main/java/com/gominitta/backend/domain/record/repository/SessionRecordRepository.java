package com.gominitta.backend.domain.record.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.gominitta.backend.domain.record.entity.SessionRecord;
import com.gominitta.backend.domain.record.entity.enums.RecordType;

public interface SessionRecordRepository extends JpaRepository<SessionRecord, Long> {

	List<SessionRecord> findBySessionIdOrderByCreatedAtAsc(Long sessionId);

	List<SessionRecord> findBySessionIdAndRecordTypeOrderByCreatedAtAsc(Long sessionId, RecordType recordType);
}

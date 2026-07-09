package com.gominitta.backend.domain.dailymessage.repository;

import java.time.LocalDateTime;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.gominitta.backend.domain.dailymessage.entity.DailyMessage;

public interface DailyMessageRepository extends JpaRepository<DailyMessage, Long> {

	@Query("SELECT m FROM DailyMessage m WHERE m.assignedAt >= :startOfToday")
	Optional<DailyMessage> findTodaysMessage(@Param("startOfToday") LocalDateTime startOfToday);

	@Query(value = "SELECT * FROM daily_messages WHERE assigned_at IS NULL ORDER BY RAND() LIMIT 1", nativeQuery = true)
	Optional<DailyMessage> findRandomAvailable();

	@Modifying(clearAutomatically = true)
	@Query("UPDATE DailyMessage m SET m.assignedAt = NULL WHERE m.assignedAt < :cutoff")
	void resetOldMessages(@Param("cutoff") LocalDateTime cutoff);
}

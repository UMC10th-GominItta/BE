package com.gominitta.backend.domain.worry.repository;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.gominitta.backend.domain.worry.entity.Worry;

public interface WorryRepository extends JpaRepository<Worry, Long> {
	List<Worry> findAllByUser_IdAndIsDeletedFalse(Long userId);

	// 걱정 리마인드 대상: 예약 시작 시각이 범위 안인 걱정
	List<Worry> findByScheduledStartAtBetween(LocalDateTime start, LocalDateTime end);
}

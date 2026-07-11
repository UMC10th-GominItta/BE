package com.gominitta.backend.global.scheduler;

import java.time.LocalDateTime;

import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import com.gominitta.backend.domain.user.repository.UserRepository;

import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class UserCleanupScheduler {

	private final UserRepository userRepository;

	// 매일 새벽 03:00 실행: 탈퇴 후 30일 경과 유저 하드딜리트
	@Scheduled(cron = "0 0 3 * * *")
	@Transactional
	public void hardDeleteExpiredUsers() {
		LocalDateTime cutoff = LocalDateTime.now().minusDays(30);
		userRepository.hardDeleteBefore(cutoff);
	}
}

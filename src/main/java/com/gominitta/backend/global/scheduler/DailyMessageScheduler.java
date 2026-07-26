package com.gominitta.backend.global.scheduler;

import java.time.LocalDate;
import java.time.LocalDateTime;

import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import com.gominitta.backend.domain.dailymessage.entity.DailyMessage;
import com.gominitta.backend.domain.dailymessage.repository.DailyMessageRepository;
import com.gominitta.backend.domain.user.repository.UserRepository;

import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class DailyMessageScheduler {

	private final DailyMessageRepository dailyMessageRepository;
	private final UserRepository userRepository;

	// 매일 00:00 실행: 그제 사용 메시지 초기화 + 오늘 메시지 사전 배정 + 전체 유저 FK 업데이트
	@Scheduled(cron = "0 0 0 * * *")
	@Transactional
	public void resetAndAssign() {
		LocalDateTime cutoff = LocalDate.now().minusDays(1).atStartOfDay();
		dailyMessageRepository.resetOldMessages(cutoff);

		dailyMessageRepository.findRandomAvailable().ifPresent(message -> {
			message.assign();
			dailyMessageRepository.saveAndFlush(message);
			userRepository.updateDailyMessageForAll(message);
		});
	}
}

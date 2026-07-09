package com.gominitta.backend.global.scheduler;

import java.time.LocalDate;
import java.time.LocalDateTime;

import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import com.gominitta.backend.domain.dailymessage.entity.DailyMessage;
import com.gominitta.backend.domain.dailymessage.repository.DailyMessageRepository;

import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class DailyMessageScheduler {

	private final DailyMessageRepository dailyMessageRepository;

	// 매일 00:00 실행: 그제 사용 메시지 초기화 + 오늘 메시지 사전 배정
	@Scheduled(cron = "0 0 0 * * *")
	@Transactional
	public void resetAndAssign() {
		// 어제 시작 이전(= 그제 이전) assigned_at을 null로 초기화
		LocalDateTime cutoff = LocalDate.now().minusDays(1).atStartOfDay();
		dailyMessageRepository.resetOldMessages(cutoff);

		// 오늘 메시지 사전 배정
		dailyMessageRepository.findRandomAvailable()
			.ifPresent(DailyMessage::assign);
	}
}

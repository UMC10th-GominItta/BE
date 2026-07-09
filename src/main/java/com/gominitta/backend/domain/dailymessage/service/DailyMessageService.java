package com.gominitta.backend.domain.dailymessage.service;

import java.time.LocalDate;
import java.time.LocalDateTime;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.gominitta.backend.domain.dailymessage.entity.DailyMessage;
import com.gominitta.backend.domain.dailymessage.exception.DailyMessageErrorCode;
import com.gominitta.backend.domain.dailymessage.repository.DailyMessageRepository;
import com.gominitta.backend.global.common.exception.GeneralException;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class DailyMessageService {

	private final DailyMessageRepository dailyMessageRepository;

	@Transactional
	public DailyMessage getTodaysMessage() {
		LocalDateTime startOfToday = LocalDate.now().atStartOfDay();

		return dailyMessageRepository.findTodaysMessage(startOfToday)
			.orElseGet(() -> {
				DailyMessage message = dailyMessageRepository.findRandomAvailable()
					.orElseThrow(() -> new GeneralException(DailyMessageErrorCode.DAILY_MESSAGE_NOT_FOUND));
				message.assign();
				return message;
			});
	}
}

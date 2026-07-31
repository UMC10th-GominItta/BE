package com.gominitta.backend.domain.notification.scheduler;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import com.gominitta.backend.domain.notification.entity.Notification;
import com.gominitta.backend.domain.notification.entity.NotificationType;
import com.gominitta.backend.domain.notification.entity.ReferenceType;
import com.gominitta.backend.domain.notification.repository.NotificationRepository;
import com.gominitta.backend.domain.notification.repository.NotificationSettingRepository;
import com.gominitta.backend.domain.session.entity.MindSession;
import com.gominitta.backend.domain.session.entity.enums.SessionStatus;
import com.gominitta.backend.domain.session.repository.SessionRepository;
import com.gominitta.backend.domain.worry.entity.Worry;
import com.gominitta.backend.domain.worry.repository.WorryRepository;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
@RequiredArgsConstructor
public class NotificationScheduler {

	private final WorryRepository worryRepository;
	private final SessionRepository sessionRepository;
	private final NotificationRepository notificationRepository;
	private final NotificationSettingRepository settingRepository;

	// 걱정 리마인드
	private static final int WORRY_REMINDER_MINUTES_BEFORE = 20;

	// 1분마다 실행, 지금 발화해야 할 알림 대상을 찾아 notifications 테이블에 생성
	@Scheduled(cron = "0 * * * * *") // 매 분 0초에 실행
	@Transactional
	public void dispatchNotifications() {
		LocalDateTime now = LocalDateTime.now();
		LocalDateTime windowStart = now.withSecond(0).withNano(0);
		LocalDateTime windowEnd = windowStart.plusMinutes(1);

		dispatchWorryReminders(windowStart, windowEnd);
		dispatchSessionStartAlarms(windowStart, windowEnd);
	}

	// 걱정 예약 리마인드
	private void dispatchWorryReminders(LocalDateTime windowStart, LocalDateTime windowEnd) {
		LocalDateTime targetStart = windowStart.plusMinutes(WORRY_REMINDER_MINUTES_BEFORE);
		LocalDateTime targetEnd = windowEnd.plusMinutes(WORRY_REMINDER_MINUTES_BEFORE);

		List<Worry> worries = worryRepository.findByScheduledStartAtBetween(targetStart, targetEnd);

		for (Worry worry : worries) {
			Long userId = worry.getUser().getId();

			// 중복 발화 방지
			if (notificationRepository.existsByReferenceTypeAndReferenceIdAndType(
				ReferenceType.WORRY, worry.getId(), NotificationType.WORRY_REMINDER)) {
				continue;
			}
			// 유저 설정 OFF면 스킵
			if (!isWorryReminderEnabled(userId)) {
				continue;
			}

			Notification notification = Notification.builder()
				.userId(userId)
				.type(NotificationType.WORRY_REMINDER)
				.title("곧 마주할 시간이에요")
				.body("예약한 걱정을 마주할 시간이 다가오고 있어요.")
				.referenceId(worry.getId())
				.referenceType(ReferenceType.WORRY)
				.build();
			notificationRepository.save(notification);
			log.info("걱정 리마인드 발화 - worryId={}, userId={}", worry.getId(), userId);
		}
	}

	// 마음 세션 시작 알림
	private void dispatchSessionStartAlarms(LocalDateTime windowStart, LocalDateTime windowEnd) {
		List<MindSession> sessions = sessionRepository.findByScheduledStartAtBetweenAndStatus(
			windowStart, windowEnd, SessionStatus.SCHEDULED);

		for (MindSession session : sessions) {
			Long userId = session.getUserId();

			if (notificationRepository.existsByReferenceTypeAndReferenceIdAndType(
				ReferenceType.SESSION, session.getMindSessionId(), NotificationType.SESSION_REMINDER)) {
				continue;
			}
			if (!isSessionStartEnabled(userId)) {
				continue;
			}

			Notification notification = Notification.builder()
				.userId(userId)
				.type(NotificationType.SESSION_REMINDER)
				.title("마음 세션 시작 시간이에요")
				.body("지금 예약한 마음 세션을 시작해보세요.")
				.referenceId(session.getMindSessionId())
				.referenceType(ReferenceType.SESSION)
				.build();
			notificationRepository.save(notification);
			log.info("세션 시작 알림 발화 - sessionId={}, userId={}", session.getMindSessionId(), userId);
		}
	}

	private boolean isWorryReminderEnabled(Long userId) {
		return settingRepository.findByUserId(userId)
			.map(s -> s.isWorryReminderEnabled())
			.orElse(true);
	}

	private boolean isSessionStartEnabled(Long userId) {
		return settingRepository.findByUserId(userId)
			.map(s -> s.isSessionStartEnabled())
			.orElse(true);
	}
}

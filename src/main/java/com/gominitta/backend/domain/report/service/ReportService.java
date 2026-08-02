package com.gominitta.backend.domain.report.service;

import java.time.LocalDateTime;
import java.util.Comparator;
import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.gominitta.backend.domain.report.dto.response.AnxietyGapResponseDTO;
import com.gominitta.backend.domain.report.dto.response.WorryThemeResponseDTO;
import com.gominitta.backend.domain.report.dto.response.WorryThemeResponseDTO.ThemeCount;
import com.gominitta.backend.domain.report.enums.PeriodType;
import com.gominitta.backend.domain.session.repository.SessionRepository;
import com.gominitta.backend.domain.session.repository.SessionRepository.AnxietyGapAggregate;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class ReportService {

	private final SessionRepository sessionRepository;

	public WorryThemeResponseDTO getWorryThemes(Long userId, PeriodType period) {
		LocalDateTime from = LocalDateTime.now().minusDays(period.getDays());

		List<ThemeCount> themes = sessionRepository.findThemeCategoryCounts(userId, from).stream()
			.map(row -> new ThemeCount(row.getThemeCategory(), row.getCount()))
			.sorted(Comparator.comparingLong(ThemeCount::count).reversed())
			.toList();

		if (themes.isEmpty()) {
			return WorryThemeResponseDTO.empty();
		}
		return WorryThemeResponseDTO.of(themes);
	}

	public AnxietyGapResponseDTO getAnxietyGap(Long userId, PeriodType period) {
		LocalDateTime from = LocalDateTime.now().minusDays(period.getDays());

		AnxietyGapAggregate agg = sessionRepository.findAnxietyGap(userId, from);

		if (agg.getSessionCount() == 0 || agg.getAvgBefore() == null || agg.getAvgAfter() == null) {
			return AnxietyGapResponseDTO.empty();
		}
		return AnxietyGapResponseDTO.of(agg.getAvgBefore(), agg.getAvgAfter());
	}
}

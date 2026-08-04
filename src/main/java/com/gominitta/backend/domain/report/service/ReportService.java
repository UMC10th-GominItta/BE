package com.gominitta.backend.domain.report.service;

import java.time.DayOfWeek;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.gominitta.backend.domain.report.dto.response.AnxietyGapResponseDTO;
import com.gominitta.backend.domain.report.dto.response.WorryThemeResponseDTO;
import com.gominitta.backend.domain.report.dto.response.WorryThemeResponseDTO.ThemeCount;
import com.gominitta.backend.domain.report.dto.response.WorryTimelineResponseDTO;
import com.gominitta.backend.domain.report.dto.response.WorryTimelineResponseDTO.Cell;
import com.gominitta.backend.domain.report.enums.PeriodType;
import com.gominitta.backend.domain.report.enums.TimeSlot;
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

	public WorryTimelineResponseDTO getWorryTimeline(Long userId, PeriodType period) {
		LocalDateTime from = LocalDateTime.now().minusDays(period.getDays());
		List<LocalDateTime> createdAts = sessionRepository.findCreatedAtsForTimeline(userId, from);

		if (createdAts.isEmpty()) {
			return WorryTimelineResponseDTO.empty();
		}

		// (요일, 시간대) → count 집계
		Map<String, Long> counter = new LinkedHashMap<>();
		for (LocalDateTime dt : createdAts) {
			String key = dt.getDayOfWeek().name() + "|" + TimeSlot.from(dt.getHour()).name();
			counter.merge(key, 1L, Long::sum);
		}

		// 7x4 = 28칸 전부 채우기 (없는 칸은 0)
		List<Cell> cells = new ArrayList<>();
		for (DayOfWeek day : DayOfWeek.values()) {
			for (TimeSlot slot : TimeSlot.values()) {
				long count = counter.getOrDefault(day.name() + "|" + slot.name(), 0L);
				cells.add(new Cell(day, slot, count));
			}
		}

		// 최다 셀 상위 2개 (count>0인 것만)
		List<Cell> topCells = cells.stream()
			.filter(cell -> cell.count() > 0)
			.sorted(Comparator.comparingLong(Cell::count).reversed())
			.limit(2)
			.toList();

		return WorryTimelineResponseDTO.of(cells, topCells);
	}
}

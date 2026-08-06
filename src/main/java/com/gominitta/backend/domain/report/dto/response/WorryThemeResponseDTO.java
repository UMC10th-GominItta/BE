package com.gominitta.backend.domain.report.dto.response;

import java.util.List;

import com.gominitta.backend.domain.session.entity.enums.ThemeCategory;

public record WorryThemeResponseDTO(
	boolean hasEnoughData,
	ThemeCategory topTheme,
	long totalCount,
	List<ThemeCount> themes
) {

	public record ThemeCount(
		ThemeCategory theme,
		long count
	) {
	}

	public static WorryThemeResponseDTO empty() {
		return new WorryThemeResponseDTO(false, null, 0L, List.of());
	}

	public static WorryThemeResponseDTO of(List<ThemeCount> sortedThemes) {
		long total = sortedThemes.stream()
			.mapToLong(ThemeCount::count)
			.sum();
		ThemeCategory top = sortedThemes.isEmpty() ? null : sortedThemes.get(0).theme();
		return new WorryThemeResponseDTO(true, top, total, sortedThemes);
	}
}

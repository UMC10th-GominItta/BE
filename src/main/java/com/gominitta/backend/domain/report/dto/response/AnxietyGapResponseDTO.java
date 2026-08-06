package com.gominitta.backend.domain.report.dto.response;

public record AnxietyGapResponseDTO(
	boolean hasEnoughData,
	int avgBefore,
	int avgAfter,
	int gap,
	boolean improved
) {

	public static AnxietyGapResponseDTO empty() {
		return new AnxietyGapResponseDTO(false, 0, 0, 0, false);
	}

	public static AnxietyGapResponseDTO of(double avgBefore, double avgAfter) {
		int before = (int)Math.round(avgBefore);
		int after = (int)Math.round(avgAfter);
		int gap = before - after;
		return new AnxietyGapResponseDTO(true, before, after, gap, gap > 0);
	}
}
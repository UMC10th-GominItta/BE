package com.gominitta.backend.domain.report.dto.response;

import java.time.DayOfWeek;
import java.util.List;

import com.gominitta.backend.domain.report.enums.TimeSlot;

public record WorryTimelineResponseDTO(
	boolean hasEnoughData,
	List<Cell> cells,
	List<Cell> topCells
) {

	public record Cell(
		DayOfWeek dayOfWeek,
		TimeSlot timeSlot,
		long count
	) {
	}

	public static WorryTimelineResponseDTO empty() {
		return new WorryTimelineResponseDTO(false, List.of(), List.of());
	}

	public static WorryTimelineResponseDTO of(List<Cell> cells, List<Cell> topCells) {
		return new WorryTimelineResponseDTO(true, cells, topCells);
	}
}

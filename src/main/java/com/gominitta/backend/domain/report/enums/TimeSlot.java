package com.gominitta.backend.domain.report.enums;

import com.fasterxml.jackson.annotation.JsonValue;

public enum TimeSlot {
	DAWN("밤", 0, 6),      // 00-06
	MORNING("아침", 6, 12),   // 06-12
	AFTERNOON("오후", 12, 18), // 12-18
	EVENING("저녁", 18, 24);  // 18-24

	private final String label;
	private final int startHour;
	private final int endHour;

	TimeSlot(String label, int startHour, int endHour) {
		this.label = label;
		this.startHour = startHour;
		this.endHour = endHour;
	}

	@JsonValue
	public String getLabel() {
		return label;
	}

	public static TimeSlot from(int hour) {
		for (TimeSlot slot : values()) {
			if (hour >= slot.startHour && hour < slot.endHour) {
				return slot;
			}
		}
		return DAWN; // 안전장치
	}
}

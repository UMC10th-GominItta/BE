package com.gominitta.backend.domain.record.entity.enums;

import com.fasterxml.jackson.annotation.JsonValue;

public enum RecordType {
	TEXT,
	VOICE,
	HANDWRITING;

	@JsonValue
	public String toJson() {
		return name().toLowerCase();
	}
}

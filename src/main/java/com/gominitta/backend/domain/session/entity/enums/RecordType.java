package com.gominitta.backend.domain.session.entity.enums;

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

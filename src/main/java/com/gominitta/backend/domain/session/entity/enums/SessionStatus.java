package com.gominitta.backend.domain.session.entity.enums;

import com.fasterxml.jackson.annotation.JsonValue;

public enum SessionStatus {
	SCHEDULED,
	INCOMPLETE,
	IN_PROGRESS,
	COMPLETED;

	@JsonValue
	public String toJson() {
		return name().toLowerCase();
	}
}

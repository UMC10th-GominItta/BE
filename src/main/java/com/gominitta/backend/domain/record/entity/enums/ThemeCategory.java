package com.gominitta.backend.domain.record.entity.enums;

import com.fasterxml.jackson.annotation.JsonValue;

public enum ThemeCategory {
	CAREER("진로"),
	STUDY("학업"),
	FAMILY("가족"),
	HEALTH("건강"),
	MONEY("돈"),
	JOB("취업"),
	RELATIONSHIP("인간관계");

	private final String label;

	ThemeCategory(String label) {
		this.label = label;
	}

	@JsonValue
	public String getLabel() {
		return label;
	}
}

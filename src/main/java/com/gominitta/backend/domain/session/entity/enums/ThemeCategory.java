package com.gominitta.backend.domain.session.entity.enums;

import com.fasterxml.jackson.annotation.JsonValue;

public enum ThemeCategory {
	CAREER("진로"),
	RELATIONSHIP("관계"),
	STUDY("학업"),
	HEALTH("건강"),
	FAMILY("가족"),
	JOB("취업"),
	MONEY("돈"),
	PRESENTATION("발표");

	private final String label;

	ThemeCategory(String label) {
		this.label = label;
	}

	@JsonValue
	public String getLabel() {
		return label;
	}
}

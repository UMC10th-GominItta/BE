package com.gominitta.backend.domain.session.entity.enums;

import com.fasterxml.jackson.annotation.JsonValue;

public enum ThemeCategory {
	CAREER("진로"),
	JOB("취업"),
	STUDY("학업"),
	MONEY("돈"),
	FAMILY("가족"),
	RELATIONSHIP("관계"),
	HEALTH("건강"),
	OTHER("기타");

	private final String label;

	ThemeCategory(String label) {
		this.label = label;
	}

	@JsonValue
	public String getLabel() {
		return label;
	}
}

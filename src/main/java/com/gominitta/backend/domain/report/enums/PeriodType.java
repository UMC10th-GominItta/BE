package com.gominitta.backend.domain.report.enums;

import java.util.Arrays;

import com.gominitta.backend.domain.report.exception.ReportErrorCode;
import com.gominitta.backend.global.common.exception.GeneralException;

public enum PeriodType {
	LAST_14_DAYS("14d", 14),
	LAST_30_DAYS("30d", 30),
	LAST_60_DAYS("60d", 60);

	private final String code;
	private final int days;

	PeriodType(String code, int days) {
		this.code = code;
		this.days = days;
	}

	public int getDays() {
		return days;
	}

	public static PeriodType from(String code) {
		return Arrays.stream(values())
			.filter(period -> period.code.equalsIgnoreCase(code))
			.findFirst()
			.orElseThrow(() -> new GeneralException(ReportErrorCode.INVALID_PERIOD));
	}
}

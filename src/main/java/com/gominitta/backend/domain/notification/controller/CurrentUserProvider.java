package com.gominitta.backend.domain.notification.controller;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

public final class CurrentUserProvider {

	private CurrentUserProvider() {
	}

	public static Long getCurrentUserId() {
		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
		if (authentication == null || authentication.getName() == null) {
			throw new IllegalStateException("인증 정보가 없습니다. (A 의 JWT 필터 확인 필요)");
		}
		return Long.parseLong(authentication.getName());
	}
}
package com.gominitta.backend.global.auth.util;

import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

import com.gominitta.backend.domain.user.exception.UserErrorCode;
import com.gominitta.backend.global.common.exception.GeneralException;

public class SecurityUtil {

	private SecurityUtil() {
	}

	public static Long getCurrentUserId() {
		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

		if (authentication == null
			|| !authentication.isAuthenticated()
			|| authentication instanceof AnonymousAuthenticationToken) {
			throw new GeneralException(UserErrorCode.UNAUTHORIZED);
		}

		Object principal = authentication.getPrincipal();
		if (principal instanceof Long userId) {
			return userId;
		}

		throw new GeneralException(UserErrorCode.UNAUTHORIZED);
	}
}

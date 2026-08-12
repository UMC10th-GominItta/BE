package com.gominitta.backend.domain.recipe.exception;

import org.springframework.http.HttpStatus;

import com.gominitta.backend.global.common.exception.BaseErrorCode;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum RecipeErrorCode implements BaseErrorCode {

	// 400 Bad Request
	RECIPE_INVALID_SCOPE(HttpStatus.BAD_REQUEST, "RECIPE_40001", "scope는 mine 또는 system만 허용됩니다."),
	RECIPE_INVALID_TITLE(HttpStatus.BAD_REQUEST, "RECIPE_40002", "제목은 공백일 수 없습니다."),
	RECIPE_INVALID_DESCRIPTION(HttpStatus.BAD_REQUEST, "RECIPE_40003", "설명은 공백일 수 없습니다."),

	// 403 Forbidden
	RECIPE_FORBIDDEN(HttpStatus.FORBIDDEN, "RECIPE_40301", "본인의 레시피만 수정할 수 있습니다."),
	RECIPE_DELETE_FORBIDDEN(HttpStatus.FORBIDDEN, "RECIPE_40302", "본인의 레시피만 삭제할 수 있습니다."),
	RECIPE_VIEW_FORBIDDEN(HttpStatus.FORBIDDEN, "RECIPE_40305", "본인의 레시피만 조회할 수 있습니다."),
	RECIPE_LOG_FORBIDDEN(HttpStatus.FORBIDDEN, "RECIPE_40303", "본인의 실행 기록만 처리할 수 있습니다."),
	RECIPE_LOG_CREATE_FORBIDDEN(HttpStatus.FORBIDDEN, "RECIPE_40304", "본인의 레시피만 실행할 수 있습니다."),

	// 404 Not Found
	RECIPE_NOT_FOUND(HttpStatus.NOT_FOUND, "RECIPE_40401", "레시피를 찾을 수 없습니다."),
	RECIPE_LOG_NOT_FOUND(HttpStatus.NOT_FOUND, "RECIPE_40402", "실행 기록을 찾을 수 없습니다."),

	// 409 Conflict
	RECIPE_LOG_ALREADY_COMPLETED(HttpStatus.CONFLICT, "RECIPE_40901", "이미 완료 처리된 기록입니다."),
	RECIPE_ALREADY_DELETED(HttpStatus.CONFLICT, "RECIPE_40902", "이미 삭제된 레시피입니다.");

	private final HttpStatus status;
	private final String code;
	private final String message;
}

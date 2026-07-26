package com.gominitta.backend.global.common.exception;

import java.util.stream.Collectors;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;
import org.springframework.web.multipart.MaxUploadSizeExceededException;

import com.gominitta.backend.domain.record.exception.RecordErrorCode;
import com.gominitta.backend.global.common.response.ApiResponse;

import jakarta.validation.ConstraintViolation;
import jakarta.validation.ConstraintViolationException;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler {

	@ExceptionHandler(GeneralException.class)
	public ResponseEntity<ApiResponse<Void>> handleGeneral(GeneralException ex) {
		BaseErrorCode ec = ex.getErrorCode();

		log.warn("[GeneralException] Code: {}, Message: {}", ec.getCode(), ec.getMessage());

		return ResponseEntity
			.status(ec.getStatus())
			.body(ApiResponse.fail(ec.getCode(), ec.getMessage()));
	}

	@ExceptionHandler(MethodArgumentNotValidException.class)
	public ResponseEntity<ApiResponse<Void>> handleMethodArgumentNotValid(
		MethodArgumentNotValidException ex
	) {
		String message = ex.getBindingResult().getFieldErrors().stream()
			.map(this::formatFieldError)
			.collect(Collectors.joining(", "));

		log.warn("[Validation Error] {}", message);

		return ResponseEntity
			.status(HttpStatus.BAD_REQUEST)
			.body(ApiResponse.fail(
				"COMMON_400_VALIDATION",
				message.isBlank() ? "요청 값이 올바르지 않습니다." : message
			));
	}

	@ExceptionHandler(MethodArgumentTypeMismatchException.class)
	public ResponseEntity<ApiResponse<Void>> handleTypeMismatch(
		MethodArgumentTypeMismatchException ex
	) {
		String message = String.format("'%s' 값이 올바르지 않습니다.", ex.getName());
		log.warn("[Type Mismatch] Field: {}, Value: {}", ex.getName(), ex.getValue());

		return ResponseEntity
			.status(HttpStatus.BAD_REQUEST)
			.body(ApiResponse.fail("COMMON_400_TYPE_MISMATCH", message));
	}

	@ExceptionHandler(HttpMessageNotReadableException.class)
	public ResponseEntity<ApiResponse<Void>> handleNotReadable(
		HttpMessageNotReadableException ex
	) {
		log.warn("[JSON Parse Error] {}", ex.getMessage());

		return ResponseEntity
			.status(HttpStatus.BAD_REQUEST)
			.body(ApiResponse.fail(
				"COMMON_400_BODY_NOT_READABLE",
				"요청 본문(JSON)을 올바르게 작성해 주세요."
			));
	}

	@ExceptionHandler(ConstraintViolationException.class)
	public ResponseEntity<ApiResponse<Void>> handleConstraintViolation(
		ConstraintViolationException ex
	) {
		String message = ex.getConstraintViolations().stream()
			.map(ConstraintViolation::getMessage)
			.collect(Collectors.joining(", "));

		log.warn("[Constraint Violation] {}", message);

		return ResponseEntity
			.status(HttpStatus.BAD_REQUEST)
			.body(ApiResponse.fail(
				"COMMON_400_CONSTRAINT",
				message.isBlank() ? "요청 값이 올바르지 않습니다." : message
			));
	}

	@ExceptionHandler(MaxUploadSizeExceededException.class)
	public ResponseEntity<ApiResponse<Void>> handleMaxUploadSize(MaxUploadSizeExceededException ex) {
		BaseErrorCode ec = RecordErrorCode.FILE_TOO_LARGE;

		log.warn("[MaxUploadSizeExceeded] {}", ex.getMessage());

		return ResponseEntity
			.status(ec.getStatus())
			.body(ApiResponse.fail(ec.getCode(), ec.getMessage()));
	}

	@ExceptionHandler(Exception.class)
	public ResponseEntity<ApiResponse<Void>> handleException(Exception ex) {
		log.error("[Unhandled Exception] ", ex);

		return ResponseEntity
			.status(500)
			.body(ApiResponse.fail("COMMON_500", "서버 오류가 발생했습니다."));
	}

	private String formatFieldError(FieldError fe) {
		return String.format("[%s] %s", fe.getField(),
			(fe.getDefaultMessage() != null ? fe.getDefaultMessage() : "입력값이 올바르지 않습니다."));
	}

}

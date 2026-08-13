package com.gominitta.backend.domain.record.client;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public record GoogleVisionAnnotateResponseDTO(
	List<AnnotateResponse> responses
) {

	@JsonIgnoreProperties(ignoreUnknown = true)
	public record AnnotateResponse(
		FullTextAnnotation fullTextAnnotation,
		VisionError error
	) {
	}

	@JsonIgnoreProperties(ignoreUnknown = true)
	public record FullTextAnnotation(
		String text
	) {
	}

	@JsonIgnoreProperties(ignoreUnknown = true)
	public record VisionError(
		Integer code,
		String message
	) {
	}
}

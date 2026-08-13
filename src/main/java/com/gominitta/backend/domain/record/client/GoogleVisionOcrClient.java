package com.gominitta.backend.domain.record.client;

import java.io.IOException;
import java.time.Duration;
import java.util.Base64;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.MediaType;
import org.springframework.http.client.SimpleClientHttpRequestFactory;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClient;
import org.springframework.web.client.RestClientException;
import org.springframework.web.multipart.MultipartFile;

import com.gominitta.backend.domain.record.exception.RecordErrorCode;
import com.gominitta.backend.global.common.exception.GeneralException;

import jakarta.annotation.PostConstruct;

@Component
public class GoogleVisionOcrClient {

	private static final String ANNOTATE_URL = "https://vision.googleapis.com/v1/images:annotate";
	private static final String FEATURE_TYPE = "DOCUMENT_TEXT_DETECTION";
	private static final List<String> LANGUAGE_HINTS = List.of("ko");
	private static final int MAX_ATTEMPTS = 2;

	@Value("${google.vision.api-key}")
	private String apiKey;

	private RestClient restClient;

	@PostConstruct
	public void init() {
		SimpleClientHttpRequestFactory factory = new SimpleClientHttpRequestFactory();
		factory.setConnectTimeout(Duration.ofSeconds(5));
		factory.setReadTimeout(Duration.ofSeconds(60));
		this.restClient = RestClient.builder().requestFactory(factory).build();
	}

	public String extractText(MultipartFile file) {
		String base64Content;
		try {
			base64Content = Base64.getEncoder().encodeToString(file.getBytes());
		} catch (IOException ex) {
			throw new GeneralException(RecordErrorCode.OCR_FAILED);
		}

		String text = null;
		for (int attempt = 1; attempt <= MAX_ATTEMPTS; attempt++) {
			text = requestAnnotation(base64Content);
			if (text != null && !text.isBlank()) {
				return text.trim();
			}
		}

		throw new GeneralException(RecordErrorCode.OCR_FAILED);
	}

	private String requestAnnotation(String base64Content) {
		try {
			Map<String, Object> body = Map.of(
				"requests", List.of(
					Map.of(
						"image", Map.of("content", base64Content),
						"features", List.of(Map.of("type", FEATURE_TYPE)),
						"imageContext", Map.of("languageHints", LANGUAGE_HINTS)
					)
				)
			);

			GoogleVisionAnnotateResponseDTO response = restClient.post()
				.uri(ANNOTATE_URL + "?key=" + apiKey)
				.contentType(MediaType.APPLICATION_JSON)
				.body(body)
				.retrieve()
				.body(GoogleVisionAnnotateResponseDTO.class);

			return extractContent(response);
		} catch (RestClientException ex) {
			return null;
		}
	}

	private String extractContent(GoogleVisionAnnotateResponseDTO response) {
		if (response == null || response.responses() == null || response.responses().isEmpty()) {
			return null;
		}
		GoogleVisionAnnotateResponseDTO.AnnotateResponse first = response.responses().get(0);
		if (first.error() != null) {
			return null;
		}
		if (first.fullTextAnnotation() == null) {
			return null;
		}
		return first.fullTextAnnotation().text();
	}
}

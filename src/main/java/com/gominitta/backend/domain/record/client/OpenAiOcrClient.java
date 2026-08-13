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
public class OpenAiOcrClient {

	private static final String CHAT_COMPLETIONS_URL = "https://api.openai.com/v1/chat/completions";
	private static final String UNREADABLE_SENTINEL = "[판독불가]";
	private static final String EXTRACTION_PROMPT =
		"이 이미지에 있는 손글씨 텍스트를 그대로 추출해서 반환해. 다른 설명 없이 인식된 텍스트만 출력해. "
			+ "이미지에 없는 내용을 추측하거나 지어내지 마. "
			+ "글씨가 없거나, 읽을 수 없을 정도로 흐릿하거나 애매하면 다른 텍스트를 만들어내지 말고 정확히 \""
			+ UNREADABLE_SENTINEL + "\" 라고만 출력해.";
	private static final int MAX_ATTEMPTS = 2;

	@Value("${openai.api-key}")
	private String apiKey;

	@Value("${openai.ocr.model}")
	private String model;

	private RestClient restClient;

	@PostConstruct
	public void init() {
		SimpleClientHttpRequestFactory factory = new SimpleClientHttpRequestFactory();
		factory.setConnectTimeout(Duration.ofSeconds(5));
		factory.setReadTimeout(Duration.ofSeconds(60));
		this.restClient = RestClient.builder().requestFactory(factory).build();
	}

	public String extractText(MultipartFile file) {
		String dataUri;
		try {
			dataUri = "data:" + file.getContentType() + ";base64,"
				+ Base64.getEncoder().encodeToString(file.getBytes());
		} catch (IOException ex) {
			throw new GeneralException(RecordErrorCode.OCR_FAILED);
		}

		String text = null;
		for (int attempt = 1; attempt <= MAX_ATTEMPTS; attempt++) {
			text = requestExtraction(dataUri);
			if (isUsableText(text)) {
				return text;
			}
		}

		throw new GeneralException(RecordErrorCode.OCR_FAILED);
	}

	private String requestExtraction(String dataUri) {
		try {
			Map<String, Object> body = Map.of(
				"model", model,
				"temperature", 0,
				"messages", List.of(
					Map.of(
						"role", "user",
						"content", List.of(
							Map.of("type", "text", "text", EXTRACTION_PROMPT),
							Map.of("type", "image_url", "image_url", Map.of("url", dataUri))
						)
					)
				)
			);

			OpenAiChatCompletionResponseDTO response = restClient.post()
				.uri(CHAT_COMPLETIONS_URL)
				.header("Authorization", "Bearer " + apiKey)
				.contentType(MediaType.APPLICATION_JSON)
				.body(body)
				.retrieve()
				.body(OpenAiChatCompletionResponseDTO.class);

			return extractContent(response);
		} catch (RestClientException ex) {
			return null;
		}
	}

	private boolean isUsableText(String text) {
		return text != null && !text.isBlank() && !text.trim().equals(UNREADABLE_SENTINEL);
	}

	private String extractContent(OpenAiChatCompletionResponseDTO response) {
		if (response == null || response.choices() == null || response.choices().isEmpty()) {
			return null;
		}
		return response.choices().get(0).message().content();
	}
}

package com.gominitta.backend.domain.worry.client;

import java.time.Duration;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.MediaType;
import org.springframework.http.client.SimpleClientHttpRequestFactory;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClientResponseException;
import org.springframework.web.client.RestClient;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.gominitta.backend.domain.record.client.OpenAiChatCompletionResponseDTO;
import com.gominitta.backend.domain.session.entity.enums.ThemeCategory;

import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
@RequiredArgsConstructor
public class OpenAiCategoryClient {

	private static final String CHAT_COMPLETIONS_URL = "https://api.openai.com/v1/chat/completions";
	private static final String CLASSIFICATION_PROMPT = """
		다음 걱정의 제목과 내용을 읽고, 가장 적절한 카테고리를 분류해줘.

		카테고리:
		CAREER(진로), JOB(취업), STUDY(학업), MONEY(돈), FAMILY(가족), RELATIONSHIP(관계), HEALTH(건강), OTHER(기타)

		제목: %s
		내용: %s
		""";

	private static final Map<String, Object> RESPONSE_FORMAT = Map.of(
		"type", "json_schema",
		"json_schema", Map.of(
			"name", "category_response",
			"strict", true,
			"schema", Map.of(
				"type", "object",
				"properties", Map.of(
					"category", Map.of(
						"type", "string",
						"enum", List.of("CAREER", "JOB", "STUDY", "MONEY", "FAMILY", "RELATIONSHIP", "HEALTH", "OTHER")
					)
				),
				"required", List.of("category"),
				"additionalProperties", false
			)
		)
	);

	@Value("${openai.api-key}")
	private String apiKey;

	@Value("${openai.category.model}")
	private String model;

	private final ObjectMapper objectMapper;
	private RestClient restClient;

	@PostConstruct
	public void init() {
		SimpleClientHttpRequestFactory factory = new SimpleClientHttpRequestFactory();
		factory.setConnectTimeout(Duration.ofSeconds(5));
		factory.setReadTimeout(Duration.ofSeconds(30));
		this.restClient = RestClient.builder().requestFactory(factory).build();
	}

	private static final int MAX_ATTEMPTS = 3;

	public ThemeCategory classify(String title, String content) {
		String prompt = CLASSIFICATION_PROMPT.formatted(title, content);
		Map<String, Object> body = Map.of(
			"model", model,
			"messages", List.of(Map.of("role", "user", "content", prompt)),
			"response_format", RESPONSE_FORMAT
		);

		for (int attempt = 1; attempt <= MAX_ATTEMPTS; attempt++) {
			try {
				OpenAiChatCompletionResponseDTO response = restClient.post()
					.uri(CHAT_COMPLETIONS_URL)
					.header("Authorization", "Bearer " + apiKey)
					.contentType(MediaType.APPLICATION_JSON)
					.body(body)
					.retrieve()
					.body(OpenAiChatCompletionResponseDTO.class);

				ThemeCategory category = parseCategory(extractContent(response));
				if (category != null) return category;

				log.warn("카테고리 분류 응답 파싱 실패 (시도 {}/{})", attempt, MAX_ATTEMPTS);
			} catch (RestClientResponseException e) {
				int status = e.getStatusCode().value();
				log.warn("OpenAI API 오류 status={} (시도 {}/{})", status, attempt, MAX_ATTEMPTS);
				if (status >= 400 && status < 500 && status != 429) {
					break; // 4xx는 재시도 의미 없음 (429 rate limit 제외)
				}
			} catch (Exception e) {
				log.warn("카테고리 분류 실패 (시도 {}/{}): {}", attempt, MAX_ATTEMPTS, e.getMessage());
			}
		}

		log.warn("카테고리 분류 최종 실패, OTHER로 대체");
		return ThemeCategory.OTHER;
	}

	private String extractContent(OpenAiChatCompletionResponseDTO response) {
		if (response == null || response.choices() == null || response.choices().isEmpty()) {
			return null;
		}
		return response.choices().get(0).message().content();
	}

	private ThemeCategory parseCategory(String json) {
		if (json == null) return ThemeCategory.OTHER;
		try {
			JsonNode node = objectMapper.readTree(json);
			String value = node.get("category").asText();
			return ThemeCategory.valueOf(value);
		} catch (Exception e) {
			return ThemeCategory.OTHER;
		}
	}
}

package com.gominitta.backend.domain.record.client;

import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClient;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.gominitta.backend.domain.record.entity.enums.ThemeCategory;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
@RequiredArgsConstructor
public class OpenAiThemeClassifier implements ThemeClassifier {

	private static final String SYSTEM_PROMPT = """
		사용자가 남긴 걱정 기록을 아래 7개 카테고리 중 하나로 분류하세요.
		CAREER(진로), STUDY(학업), FAMILY(가족), HEALTH(건강), MONEY(돈), JOB(취업), RELATIONSHIP(인간관계).
		반드시 이 중 하나만 선택해서 응답하세요.
		""";

	private static final List<String> CATEGORY_NAMES =
		List.of("CAREER", "STUDY", "FAMILY", "HEALTH", "MONEY", "JOB", "RELATIONSHIP");

	private final RestClient openAiRestClient;
	private final ObjectMapper objectMapper;

	@Value("${openai.model}")
	private String model;

	@Override
	public Optional<ThemeCategory> classify(String text) {
		try {
			String responseBody = openAiRestClient.post()
				.uri("/chat/completions")
				.body(buildRequestBody(text))
				.retrieve()
				.body(String.class);
			return parseCategory(responseBody);
		} catch (Exception e) {
			log.warn("[ThemeClassifier] LLM 분류 실패: {}", e.getMessage());
			return Optional.empty();
		}
	}

	private Map<String, Object> buildRequestBody(String text) {
		return Map.of(
			"model", model,
			"messages", List.of(
				Map.of("role", "system", "content", SYSTEM_PROMPT),
				Map.of("role", "user", "content", text)
			),
			"response_format", Map.of(
				"type", "json_schema",
				"json_schema", Map.of(
					"name", "theme_classification",
					"strict", true,
					"schema", Map.of(
						"type", "object",
						"properties", Map.of(
							"category", Map.of("type", "string", "enum", CATEGORY_NAMES)
						),
						"required", List.of("category"),
						"additionalProperties", false
					)
				)
			)
		);
	}

	private Optional<ThemeCategory> parseCategory(String responseBody) {
		try {
			JsonNode root = objectMapper.readTree(responseBody);
			String content = root.path("choices").get(0).path("message").path("content").asText();
			String category = objectMapper.readTree(content).path("category").asText();
			return Optional.of(ThemeCategory.valueOf(category));
		} catch (Exception e) {
			log.warn("[ThemeClassifier] 분류 결과 파싱 실패: {}", e.getMessage());
			return Optional.empty();
		}
	}
}

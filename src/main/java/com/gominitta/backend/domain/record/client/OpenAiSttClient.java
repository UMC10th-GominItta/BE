package com.gominitta.backend.domain.record.client;

import java.time.Duration;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.MediaType;
import org.springframework.http.client.SimpleClientHttpRequestFactory;
import org.springframework.stereotype.Component;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestClient;
import org.springframework.web.client.RestClientException;
import org.springframework.web.multipart.MultipartFile;

import com.gominitta.backend.domain.record.exception.RecordErrorCode;
import com.gominitta.backend.global.common.exception.GeneralException;

import jakarta.annotation.PostConstruct;

@Component
public class OpenAiSttClient {

	private static final String TRANSCRIPTION_URL = "https://api.openai.com/v1/audio/transcriptions";
	private static final String LANGUAGE = "ko";
	private static final String CONTEXT_PROMPT = "걱정, 마음, 감정에 대해 이야기하는 개인 음성 기록입니다.";
	private static final int MAX_ATTEMPTS = 2;

	@Value("${openai.api-key}")
	private String apiKey;

	@Value("${openai.stt.model}")
	private String model;

	private RestClient restClient;

	@PostConstruct
	public void init() {
		SimpleClientHttpRequestFactory factory = new SimpleClientHttpRequestFactory();
		factory.setConnectTimeout(Duration.ofSeconds(5));
		factory.setReadTimeout(Duration.ofSeconds(120));
		this.restClient = RestClient.builder().requestFactory(factory).build();
	}

	public String transcribe(MultipartFile file) {
		String text = null;
		for (int attempt = 1; attempt <= MAX_ATTEMPTS; attempt++) {
			text = requestTranscription(file);
			if (text != null && !text.isBlank()) {
				return text;
			}
		}

		throw new GeneralException(RecordErrorCode.STT_FAILED);
	}

	private String requestTranscription(MultipartFile file) {
		try {
			MultiValueMap<String, Object> body = new LinkedMultiValueMap<>();
			body.add("file", file.getResource());
			body.add("model", model);
			body.add("language", LANGUAGE);
			body.add("prompt", CONTEXT_PROMPT);
			body.add("temperature", 0);

			OpenAiTranscriptionResponseDTO response = restClient.post()
				.uri(TRANSCRIPTION_URL)
				.header("Authorization", "Bearer " + apiKey)
				.contentType(MediaType.MULTIPART_FORM_DATA)
				.body(body)
				.retrieve()
				.body(OpenAiTranscriptionResponseDTO.class);

			return response == null ? null : response.text();
		} catch (RestClientException ex) {
			return null;
		}
	}
}

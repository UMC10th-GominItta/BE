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
		try {
			MultiValueMap<String, Object> body = new LinkedMultiValueMap<>();
			body.add("file", file.getResource());
			body.add("model", model);

			OpenAiTranscriptionResponseDTO response = restClient.post()
				.uri(TRANSCRIPTION_URL)
				.header("Authorization", "Bearer " + apiKey)
				.contentType(MediaType.MULTIPART_FORM_DATA)
				.body(body)
				.retrieve()
				.body(OpenAiTranscriptionResponseDTO.class);

			if (response == null || response.text() == null || response.text().isBlank()) {
				throw new GeneralException(RecordErrorCode.STT_FAILED);
			}
			return response.text();
		} catch (GeneralException ex) {
			throw ex;
		} catch (RestClientException ex) {
			throw new GeneralException(RecordErrorCode.STT_FAILED);
		}
	}
}

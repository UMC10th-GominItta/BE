package com.gominitta.backend.domain.record.client;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public record OpenAiTranscriptionResponseDTO(String text) {
}

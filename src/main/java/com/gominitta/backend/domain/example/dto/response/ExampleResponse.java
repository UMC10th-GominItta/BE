package com.gominitta.backend.domain.example.dto.response;

import com.gominitta.backend.domain.example.entity.Example;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class ExampleResponse {

	private Long id;
	private String name;

	public static ExampleResponse from(Example example) {
		return ExampleResponse.builder()
			.id(example.getId())
			.name(example.getName())
			.build();
	}
}

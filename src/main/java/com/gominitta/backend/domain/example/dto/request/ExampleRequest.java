package com.gominitta.backend.domain.example.dto.request;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;

@Getter
public class ExampleRequest {

	@NotBlank
	private String name;
}

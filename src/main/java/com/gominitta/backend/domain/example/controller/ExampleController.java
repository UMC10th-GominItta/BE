package com.gominitta.backend.domain.example.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.gominitta.backend.domain.example.dto.request.ExampleRequest;
import com.gominitta.backend.domain.example.dto.response.ExampleResponse;
import com.gominitta.backend.domain.example.service.ExampleService;
import com.gominitta.backend.global.common.response.ApiResponse;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/example")
@RequiredArgsConstructor
public class ExampleController {

	private final ExampleService exampleService;

	@PostMapping
	public ResponseEntity<ApiResponse<ExampleResponse>> create(@Valid @RequestBody ExampleRequest request) {
		return ResponseEntity.ok(ApiResponse.success(exampleService.create(request)));
	}

	@GetMapping("/{id}")
	public ResponseEntity<ApiResponse<ExampleResponse>> get(@PathVariable Long id) {
		return ResponseEntity.ok(ApiResponse.success(exampleService.get(id)));
	}
}

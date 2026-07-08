package com.gominitta.backend.domain.example.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.gominitta.backend.domain.example.dto.request.ExampleRequest;
import com.gominitta.backend.domain.example.dto.response.ExampleResponse;
import com.gominitta.backend.domain.example.entity.Example;
import com.gominitta.backend.domain.example.exception.ExampleErrorCode;
import com.gominitta.backend.domain.example.repository.ExampleRepository;
import com.gominitta.backend.global.common.exception.GeneralException;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class ExampleService {

	private final ExampleRepository exampleRepository;

	@Transactional
	public ExampleResponse create(ExampleRequest request) {
		Example example = Example.builder()
			.name(request.getName())
			.build();
		return ExampleResponse.from(exampleRepository.save(example));
	}

	@Transactional(readOnly = true)
	public ExampleResponse get(Long id) {
		Example example = exampleRepository.findById(id)
			.orElseThrow(() -> new GeneralException(ExampleErrorCode.EXAMPLE_NOT_FOUND));
		return ExampleResponse.from(example);
	}
}

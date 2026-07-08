package com.gominitta.backend.domain.example.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.gominitta.backend.domain.example.entity.Example;

public interface ExampleRepository extends JpaRepository<Example, Long> {
}

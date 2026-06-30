package com.gominitta.backend.domain.example.repository;

import com.gominitta.backend.domain.example.entity.Example;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ExampleRepository extends JpaRepository<Example, Long> {
}

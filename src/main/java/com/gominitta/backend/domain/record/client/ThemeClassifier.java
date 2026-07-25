package com.gominitta.backend.domain.record.client;

import java.util.Optional;

import com.gominitta.backend.domain.record.entity.enums.ThemeCategory;

public interface ThemeClassifier {

	Optional<ThemeCategory> classify(String text);
}

package com.gominitta.backend.global.storage;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import com.gominitta.backend.domain.record.exception.RecordErrorCode;
import com.gominitta.backend.global.common.exception.GeneralException;

@Component
public class LocalVoiceFileStorage {

	@Value("${file.upload-dir}")
	private String uploadDir;

	@Value("${file.base-url}")
	private String baseUrl;

	public String store(MultipartFile file) {
		try {
			Path dir = Path.of(uploadDir);
			Files.createDirectories(dir);

			String extension = extractExtension(file.getOriginalFilename());
			String filename = UUID.randomUUID() + (extension.isEmpty() ? "" : "." + extension);

			file.transferTo(dir.resolve(filename));

			return baseUrl + "/" + filename;
		} catch (IOException e) {
			throw new GeneralException(RecordErrorCode.INTERNAL_ERROR);
		}
	}

	private String extractExtension(String filename) {
		if (filename == null) {
			return "";
		}
		int dotIndex = filename.lastIndexOf('.');
		return dotIndex == -1 ? "" : filename.substring(dotIndex + 1).toLowerCase();
	}
}

package com.gominitta.backend.global.storage;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import com.gominitta.backend.domain.record.exception.RecordErrorCode;
import com.gominitta.backend.global.common.exception.GeneralException;

import lombok.RequiredArgsConstructor;

@Component
@Profile("dev")
@RequiredArgsConstructor
public class LocalFileStorage implements FileStorage {

	@Value("${file.upload-dir}")
	private String uploadDir;

	@Value("${file.base-url}")
	private String baseUrl;

	private final MediaUrlSigner mediaUrlSigner;

	@Override
	public String store(MultipartFile file, String subDir) {
		try {
			Path dir = Path.of(uploadDir, subDir);
			Files.createDirectories(dir);

			String extension = extractExtension(file.getOriginalFilename());
			String filename = UUID.randomUUID() + (extension.isEmpty() ? "" : "." + extension);

			file.transferTo(dir.resolve(filename));

			return subDir + "/" + filename;
		} catch (IOException e) {
			throw new GeneralException(RecordErrorCode.INTERNAL_ERROR);
		}
	}

	@Override
	public String resolveUrl(String mediaKey) {
		if (mediaKey == null) {
			return null;
		}
		MediaUrlSigner.SignedQuery query = mediaUrlSigner.sign(mediaKey);
		return baseUrl + "/" + mediaKey + "?exp=" + query.expiresAt() + "&sig=" + query.signature();
	}

	private String extractExtension(String filename) {
		if (filename == null) {
			return "";
		}
		int dotIndex = filename.lastIndexOf('.');
		return dotIndex == -1 ? "" : filename.substring(dotIndex + 1).toLowerCase();
	}
}

package com.gominitta.backend.global.storage;

import java.nio.file.Path;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Profile;
import org.springframework.core.io.FileSystemResource;
import org.springframework.core.io.Resource;
import org.springframework.http.MediaType;
import org.springframework.http.MediaTypeFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.gominitta.backend.domain.record.exception.RecordErrorCode;
import com.gominitta.backend.global.common.exception.GeneralException;

import lombok.RequiredArgsConstructor;

@RestController
@Profile("dev")
@RequestMapping("/media/records")
@RequiredArgsConstructor
public class MediaController {

	@Value("${file.upload-dir}")
	private String uploadDir;

	private final MediaUrlSigner mediaUrlSigner;

	@GetMapping("/{subDir}/{filename}")
	public ResponseEntity<Resource> getMedia(
		@PathVariable String subDir,
		@PathVariable String filename,
		@RequestParam(required = false) Long exp,
		@RequestParam(required = false) String sig
	) {
		String mediaKey = subDir + "/" + filename;
		if (exp == null || sig == null || !mediaUrlSigner.isValid(mediaKey, exp, sig)) {
			throw new GeneralException(RecordErrorCode.MEDIA_LINK_INVALID);
		}

		Path root = Path.of(uploadDir).toAbsolutePath().normalize();
		Path target = root.resolve(mediaKey).normalize();
		if (!target.startsWith(root)) {
			throw new GeneralException(RecordErrorCode.MEDIA_LINK_INVALID);
		}

		Resource resource = new FileSystemResource(target);
		if (!resource.exists()) {
			throw new GeneralException(RecordErrorCode.RECORD_NOT_FOUND);
		}

		MediaType mediaType = MediaTypeFactory.getMediaType(resource).orElse(MediaType.APPLICATION_OCTET_STREAM);
		return ResponseEntity.ok().contentType(mediaType).body(resource);
	}
}

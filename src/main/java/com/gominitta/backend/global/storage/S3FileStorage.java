package com.gominitta.backend.global.storage;

import java.io.IOException;
import java.time.Duration;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import com.gominitta.backend.domain.record.exception.RecordErrorCode;
import com.gominitta.backend.global.common.exception.GeneralException;

import jakarta.annotation.PostConstruct;
import software.amazon.awssdk.auth.credentials.DefaultCredentialsProvider;
import software.amazon.awssdk.core.exception.SdkException;
import software.amazon.awssdk.core.sync.RequestBody;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.GetObjectRequest;
import software.amazon.awssdk.services.s3.model.PutObjectRequest;
import software.amazon.awssdk.services.s3.presigner.S3Presigner;
import software.amazon.awssdk.services.s3.presigner.model.GetObjectPresignRequest;

@Component
@Profile("prod")
public class S3FileStorage implements FileStorage {

	private static final Duration URL_EXPIRY = Duration.ofMinutes(10);

	@Value("${aws.s3.bucket}")
	private String bucket;

	@Value("${aws.region}")
	private String region;

	private S3Client s3Client;
	private S3Presigner s3Presigner;

	@PostConstruct
	public void init() {
		Region awsRegion = Region.of(region);
		DefaultCredentialsProvider credentialsProvider = DefaultCredentialsProvider.create();
		this.s3Client = S3Client.builder()
			.region(awsRegion)
			.credentialsProvider(credentialsProvider)
			.build();
		this.s3Presigner = S3Presigner.builder()
			.region(awsRegion)
			.credentialsProvider(credentialsProvider)
			.build();
	}

	@Override
	public String store(MultipartFile file, String subDir) {
		try {
			String extension = extractExtension(file.getOriginalFilename());
			String key = "records/" + subDir + "/" + UUID.randomUUID() + (extension.isEmpty() ? "" : "." + extension);

			s3Client.putObject(
				PutObjectRequest.builder()
					.bucket(bucket)
					.key(key)
					.contentType(file.getContentType())
					.build(),
				RequestBody.fromInputStream(file.getInputStream(), file.getSize())
			);

			return key;
		} catch (IOException | SdkException e) {
			throw new GeneralException(RecordErrorCode.INTERNAL_ERROR);
		}
	}

	@Override
	public String resolveUrl(String mediaKey) {
		if (mediaKey == null) {
			return null;
		}
		GetObjectPresignRequest presignRequest = GetObjectPresignRequest.builder()
			.signatureDuration(URL_EXPIRY)
			.getObjectRequest(GetObjectRequest.builder().bucket(bucket).key(mediaKey).build())
			.build();
		return s3Presigner.presignGetObject(presignRequest).url().toString();
	}

	private String extractExtension(String filename) {
		if (filename == null) {
			return "";
		}
		int dotIndex = filename.lastIndexOf('.');
		return dotIndex == -1 ? "" : filename.substring(dotIndex + 1).toLowerCase();
	}
}

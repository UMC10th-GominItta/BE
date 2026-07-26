package com.gominitta.backend.global.storage;

import java.io.IOException;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import com.gominitta.backend.domain.record.exception.RecordErrorCode;
import com.gominitta.backend.global.common.exception.GeneralException;

import jakarta.annotation.PostConstruct;
import software.amazon.awssdk.auth.credentials.AwsBasicCredentials;
import software.amazon.awssdk.auth.credentials.StaticCredentialsProvider;
import software.amazon.awssdk.core.exception.SdkException;
import software.amazon.awssdk.core.sync.RequestBody;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.PutObjectRequest;

@Component
@Profile("prod")
public class S3FileStorage implements FileStorage {

	@Value("${aws.s3.bucket}")
	private String bucket;

	@Value("${aws.region}")
	private String region;

	@Value("${aws.access-key}")
	private String accessKey;

	@Value("${aws.secret-key}")
	private String secretKey;

	private S3Client s3Client;

	@PostConstruct
	public void init() {
		this.s3Client = S3Client.builder()
			.region(Region.of(region))
			.credentialsProvider(StaticCredentialsProvider.create(AwsBasicCredentials.create(accessKey, secretKey)))
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

			return String.format("https://%s.s3.%s.amazonaws.com/%s", bucket, region, key);
		} catch (IOException | SdkException e) {
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

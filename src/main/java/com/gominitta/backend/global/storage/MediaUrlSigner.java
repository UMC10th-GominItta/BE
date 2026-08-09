package com.gominitta.backend.global.storage;

import java.nio.charset.StandardCharsets;
import java.security.GeneralSecurityException;
import java.security.MessageDigest;
import java.time.Instant;
import java.util.Base64;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class MediaUrlSigner {

	private static final long EXPIRY_SECONDS = 600;
	private static final String HMAC_ALGORITHM = "HmacSHA256";

	@Value("${media.signing-secret}")
	private String signingSecret;

	public SignedQuery sign(String mediaKey) {
		long expiresAt = Instant.now().getEpochSecond() + EXPIRY_SECONDS;
		return new SignedQuery(expiresAt, hmac(mediaKey, expiresAt));
	}

	public boolean isValid(String mediaKey, long expiresAt, String signature) {
		if (Instant.now().getEpochSecond() > expiresAt) {
			return false;
		}
		String expected = hmac(mediaKey, expiresAt);
		return MessageDigest.isEqual(
			expected.getBytes(StandardCharsets.UTF_8),
			signature.getBytes(StandardCharsets.UTF_8)
		);
	}

	private String hmac(String mediaKey, long expiresAt) {
		try {
			Mac mac = Mac.getInstance(HMAC_ALGORITHM);
			mac.init(new SecretKeySpec(signingSecret.getBytes(StandardCharsets.UTF_8), HMAC_ALGORITHM));
			byte[] signed = mac.doFinal((mediaKey + ":" + expiresAt).getBytes(StandardCharsets.UTF_8));
			return Base64.getUrlEncoder().withoutPadding().encodeToString(signed);
		} catch (GeneralSecurityException e) {
			throw new IllegalStateException("미디어 URL 서명에 실패했습니다.", e);
		}
	}

	public record SignedQuery(long expiresAt, String signature) {
	}
}

package com.gominitta.backend.global.oauth.kakao;

import java.time.Duration;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.client.SimpleClientHttpRequestFactory;
import org.springframework.stereotype.Component;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestClient;
import org.springframework.web.client.RestClientException;

import com.gominitta.backend.domain.auth.exception.AuthErrorCode;
import com.gominitta.backend.global.common.exception.GeneralException;
import com.gominitta.backend.global.oauth.kakao.dto.KakaoTokenInfoDTO;
import com.gominitta.backend.global.oauth.kakao.dto.KakaoUserInfoDTO;

import jakarta.annotation.PostConstruct;

@Component
public class KakaoApiClient {

	private static final String KAKAO_TOKEN_INFO_URL = "https://kapi.kakao.com/v1/user/access_token_info";
	private static final String KAKAO_USER_ME_URL = "https://kapi.kakao.com/v2/user/me";

	@Value("${kakao.app-id}")
	private Long kakaoAppId;

	private RestClient restClient;

	@PostConstruct
	public void init() {
		SimpleClientHttpRequestFactory factory = new SimpleClientHttpRequestFactory();
		factory.setConnectTimeout(Duration.ofSeconds(3));
		factory.setReadTimeout(Duration.ofSeconds(5));
		this.restClient = RestClient.builder().requestFactory(factory).build();
	}

	public void verifyTokenOrigin(String accessToken) {
		try {
			KakaoTokenInfoDTO tokenInfo = restClient.get()
				.uri(KAKAO_TOKEN_INFO_URL)
				.header("Authorization", "Bearer " + accessToken)
				.retrieve()
				.body(KakaoTokenInfoDTO.class);

			if (tokenInfo == null || !kakaoAppId.equals(tokenInfo.getAppId())) {
				throw new GeneralException(AuthErrorCode.INVALID_TOKEN_ORIGIN);
			}
		} catch (GeneralException ex) {
			throw ex;
		} catch (HttpClientErrorException ex) {
			if (HttpStatus.UNAUTHORIZED.equals(ex.getStatusCode())) {
				throw new GeneralException(AuthErrorCode.INVALID_KAKAO_TOKEN);
			}
			throw new GeneralException(AuthErrorCode.KAKAO_TOKEN_VERIFY_FAILED);
		} catch (RestClientException ex) {
			throw new GeneralException(AuthErrorCode.KAKAO_TOKEN_VERIFY_FAILED);
		}
	}

	public KakaoUserInfoDTO getUserInfo(String accessToken) {
		try {
			KakaoUserInfoDTO response = restClient.get()
				.uri(KAKAO_USER_ME_URL)
				.header("Authorization", "Bearer " + accessToken)
				.retrieve()
				.body(KakaoUserInfoDTO.class);

			if (response == null) {
				throw new GeneralException(AuthErrorCode.KAKAO_USERINFO_REQUEST_FAILED);
			}
			return response;
		} catch (GeneralException ex) {
			throw ex;
		} catch (HttpClientErrorException ex) {
			if (HttpStatus.UNAUTHORIZED.equals(ex.getStatusCode())) {
				throw new GeneralException(AuthErrorCode.INVALID_KAKAO_TOKEN);
			}
			throw new GeneralException(AuthErrorCode.KAKAO_USERINFO_REQUEST_FAILED);
		} catch (RestClientException ex) {
			throw new GeneralException(AuthErrorCode.KAKAO_USERINFO_REQUEST_FAILED);
		}
	}
}

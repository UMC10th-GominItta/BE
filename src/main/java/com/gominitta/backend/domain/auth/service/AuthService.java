package com.gominitta.backend.domain.auth.service;

import java.time.Duration;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.gominitta.backend.domain.auth.dto.request.KakaoLoginRequestDTO;
import com.gominitta.backend.domain.auth.dto.request.LogoutRequestDTO;
import com.gominitta.backend.domain.auth.dto.request.TokenRefreshRequestDTO;
import com.gominitta.backend.domain.auth.dto.response.AuthTokenResponseDTO;
import com.gominitta.backend.domain.auth.exception.AuthErrorCode;
import com.gominitta.backend.global.auth.jwt.RefreshTokenStore;
import com.gominitta.backend.domain.user.entity.User;
import com.gominitta.backend.domain.user.entity.enums.ProfileIcon;
import com.gominitta.backend.domain.user.repository.UserRepository;
import com.gominitta.backend.global.auth.jwt.JwtUtil;
import com.gominitta.backend.global.common.exception.GeneralException;
import com.gominitta.backend.global.oauth.kakao.KakaoApiClient;
import com.gominitta.backend.global.oauth.kakao.dto.KakaoUserInfoDTO;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class AuthService {

	private final KakaoApiClient kakaoApiClient;
	private final UserRepository userRepository;
	private final RefreshTokenStore refreshTokenRepository;
	private final JwtUtil jwtUtil;

	@Value("${jwt.refresh-token-expiration}")
	private long refreshTokenExpiration;

	@Transactional
	public AuthTokenResponseDTO kakaoLogin(KakaoLoginRequestDTO request) {
		kakaoApiClient.verifyTokenOrigin(request.kakaoAccessToken());
		KakaoUserInfoDTO kakaoUser = kakaoApiClient.getUserInfo(request.kakaoAccessToken());

		if (kakaoUser.getId() == null) {
			throw new GeneralException(AuthErrorCode.KAKAO_USERINFO_REQUEST_FAILED);
		}
		String kakaoId = String.valueOf(kakaoUser.getId());

		KakaoUserInfoDTO.KakaoAccount account = kakaoUser.getKakaoAccount();
		String rawNickname = (account != null && account.getProfile() != null)
			? account.getProfile().getNickname() : null;
		String nickname = (rawNickname != null && !rawNickname.isBlank()) ? rawNickname : "사용자";
		String email = (account != null) ? account.getEmail() : null;

		if (email == null) {
			throw new GeneralException(AuthErrorCode.KAKAO_EMAIL_NOT_PROVIDED);
		}

		// 탈퇴 유저 재가입
		Optional<User> deletedUser = userRepository.findByKakaoIdAndIsDeletedTrue(kakaoId);
		if (deletedUser.isPresent()) {
			User user = deletedUser.get();
			user.reactivate();
			user.updateNickname(nickname);
			user.updateEmail(email);
			return issueToken(user, false);
		}

		// 기존 유저 로그인
		Optional<User> existingUser = userRepository.findByKakaoIdAndIsDeletedFalse(kakaoId);
		if (existingUser.isPresent()) {
			return issueToken(existingUser.get(), false);
		}

		// 신규 유저 회원가입
		User newUser = User.create(kakaoId, nickname, email);
		newUser.updateProfileIcon(ProfileIcon.CHARACTER_1);
		userRepository.save(newUser);
		return issueToken(newUser, true);
	}

	@Transactional
	public AuthTokenResponseDTO refresh(TokenRefreshRequestDTO request) {
		Long userId = refreshTokenRepository.findUserIdByToken(request.refreshToken())
			.orElseThrow(() -> new GeneralException(AuthErrorCode.INVALID_REFRESH_TOKEN));

		User user = userRepository.findByIdAndIsDeletedFalse(userId)
			.orElseThrow(() -> new GeneralException(AuthErrorCode.USER_NOT_FOUND));

		refreshTokenRepository.delete(request.refreshToken());
		return issueToken(user, false);
	}

	public void logout(LogoutRequestDTO request) {
		if (!refreshTokenRepository.exists(request.refreshToken())) {
			throw new GeneralException(AuthErrorCode.INVALID_REFRESH_TOKEN);
		}
		refreshTokenRepository.delete(request.refreshToken());
	}

	private AuthTokenResponseDTO issueToken(User user, boolean isNewUser) {
		String accessToken = jwtUtil.generateAccessToken(user.getId());
		String refreshToken = jwtUtil.generateRefreshToken();
		refreshTokenRepository.save(refreshToken, user.getId(), Duration.ofMillis(refreshTokenExpiration));

		return AuthTokenResponseDTO.builder()
			.accessToken(accessToken)
			.refreshToken(refreshToken)
			.isNewUser(isNewUser)
			.build();
	}
}

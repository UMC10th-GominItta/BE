package com.gominitta.backend.global.oauth.kakao.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class KakaoTokenInfoDTO {

	@JsonProperty("app_id")
	private Long appId;
}

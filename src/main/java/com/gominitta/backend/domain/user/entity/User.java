package com.gominitta.backend.domain.user.entity;

import com.gominitta.backend.domain.dailymessage.entity.DailyMessage;
import com.gominitta.backend.domain.user.entity.enums.ProfileIcon;
import com.gominitta.backend.domain.user.entity.enums.SubscriptionTier;
import com.gominitta.backend.global.common.entity.BaseEntity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Entity
@Table(name = "users")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class User extends BaseEntity {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "user_id")
	private Long id;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "daily_messages_id")
	private DailyMessage dailyMessage;

	@Column(name = "kakao_id", nullable = false, length = 30)
	private String kakaoId;

	@Column(name = "nickname", nullable = false, length = 10)
	private String nickname;

	@Column(name = "email", length = 100)
	private String email;

	@Enumerated(EnumType.STRING)
	@Column(name = "profile_icon", length = 30)
	private ProfileIcon profileIcon;

	@Enumerated(EnumType.STRING)
	@Column(name = "subscription_tier", nullable = false, length = 10)
	private SubscriptionTier subscriptionTier = SubscriptionTier.FREE;

	public static User create(String kakaoId, String nickname, String email) {
		User user = new User();
		user.kakaoId = kakaoId;
		user.nickname = nickname;
		user.email = email;
		return user;
	}

	public void updateNickname(String nickname) {
		this.nickname = nickname;
	}

	public void updateProfileIcon(ProfileIcon profileIcon) {
		this.profileIcon = profileIcon;
	}

	public void updateDailyMessage(DailyMessage dailyMessage) {
		this.dailyMessage = dailyMessage;
	}

	public void upgrade() {
		this.subscriptionTier = SubscriptionTier.PREMIUM;
	}

	public void deactivate() {
		this.softDelete();
	}

	public void reactivate() {
		this.restore();
	}
}

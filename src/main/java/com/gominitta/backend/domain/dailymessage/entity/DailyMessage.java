package com.gominitta.backend.domain.dailymessage.entity;

import java.time.LocalDateTime;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Entity
@Table(name = "daily_messages")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class DailyMessage {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "daily_messages_id")
	private Long id;

	@Column(name = "content", length = 200)
	private String content;

	@Column(name = "assigned_at")
	private LocalDateTime assignedAt;

	public void assign() {
		this.assignedAt = LocalDateTime.now();
	}
}

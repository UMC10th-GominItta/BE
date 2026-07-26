package com.gominitta.backend.domain.record.entity;

import com.gominitta.backend.domain.record.entity.enums.RecordType;
import com.gominitta.backend.global.common.entity.BaseEntity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Entity
@Table(name = "session_records")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class SessionRecord extends BaseEntity {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "id")
	private Long sessionRecordId;

	@Column(name = "session_id", nullable = false)
	private Long sessionId;

	@Enumerated(EnumType.STRING)
	@Column(name = "record_type", nullable = false, length = 20)
	private RecordType recordType;

	@Column(name = "content_text", columnDefinition = "TEXT")
	private String contentText;

	@Column(name = "media_url", length = 500)
	private String mediaUrl;

	public static SessionRecord create(Long sessionId, RecordType recordType, String contentText, String mediaUrl) {
		SessionRecord record = new SessionRecord();
		record.sessionId = sessionId;
		record.recordType = recordType;
		record.contentText = contentText;
		record.mediaUrl = mediaUrl;
		return record;
	}

	public void updateContent(String contentText) {
		this.contentText = contentText;
	}
}

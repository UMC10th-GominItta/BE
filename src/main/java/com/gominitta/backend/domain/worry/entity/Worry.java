package com.gominitta.backend.domain.worry.entity;

import com.gominitta.backend.domain.user.entity.User;
import com.gominitta.backend.global.common.entity.BaseEntity;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;


@Getter
@Entity
@Table(name = "worry")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Worry extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String content;

    @Column(nullable = false)
    private LocalDateTime scheduledStartAt;

    @Column(nullable = false)
    private LocalDateTime scheduledEndAt;

    @Column(nullable = false)
    private Integer emotionScoreBefore;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;

    public static Worry create(User user, String content, Integer emotionScoreBefore,
                               LocalDateTime scheduledStartAt, LocalDateTime scheduledEndAt) {
        Worry worry = new Worry();
        worry.user = user;
        worry.content = content;
        worry.emotionScoreBefore = emotionScoreBefore;
        worry.scheduledStartAt = scheduledStartAt;
        worry.scheduledEndAt = scheduledEndAt;
        return worry;
    }

    public void update(String content, LocalDateTime scheduledStartAt, LocalDateTime scheduledEndAt) {
        if (content != null) this.content = content;
        if (scheduledStartAt != null) this.scheduledStartAt = scheduledStartAt;
        if (scheduledEndAt != null) this.scheduledEndAt = scheduledEndAt;
    }
}

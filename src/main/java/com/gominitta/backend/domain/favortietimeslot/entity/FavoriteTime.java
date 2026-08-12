package com.gominitta.backend.domain.favortietimeslot.entity;

import com.gominitta.backend.domain.user.entity.User;
import com.gominitta.backend.global.common.entity.BaseEntity;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import java.time.LocalTime;

@Getter
@Entity
@Table(name = "favorite_time_slots")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class FavoriteTime extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String label;

    @Column(nullable = false)
    private LocalTime startTime;

    @Column(nullable = false)
    private LocalTime endTime;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;

    public static FavoriteTime create(User user, String label, LocalTime startTime, LocalTime endTime) {
        FavoriteTime favoriteTime = new FavoriteTime();
        favoriteTime.user = user;
        favoriteTime.label = label;
        favoriteTime.startTime = startTime;
        favoriteTime.endTime = endTime;
        return favoriteTime;
    }
    public void update(String label,LocalTime startTime, LocalTime endTime) {
        if (label != null) this.label = label;
        if (startTime != null) this.startTime = startTime;
        if (endTime != null) this.endTime = endTime;
    }
}

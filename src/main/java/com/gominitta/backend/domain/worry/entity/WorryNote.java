package com.gominitta.backend.domain.worry.entity;

import com.gominitta.backend.global.common.entity.BaseEntity;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Entity
@Table(name = "worry_note")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class WorryNote extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String content;

}

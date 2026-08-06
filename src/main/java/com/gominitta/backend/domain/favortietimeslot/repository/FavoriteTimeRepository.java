package com.gominitta.backend.domain.favortietimeslot.repository;

import com.gominitta.backend.domain.favortietimeslot.entity.FavoriteTime;
import com.gominitta.backend.domain.worry.entity.Worry;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface FavoriteTimeRepository extends JpaRepository<FavoriteTime, Long> {
    List<FavoriteTime> findAllByUser_IdAndIsDeletedFalse(Long userId);
}

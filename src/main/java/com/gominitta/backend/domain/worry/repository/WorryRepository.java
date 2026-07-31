package com.gominitta.backend.domain.worry.repository;

import com.gominitta.backend.domain.worry.entity.Worry;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface WorryRepository extends JpaRepository<Worry, Long> {
    List<Worry> findAllByUser_IdAndIsDeletedFalse(Long userId);
}

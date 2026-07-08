package com.gominitta.backend.domain.user.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.gominitta.backend.domain.user.entity.User;

public interface UserRepository extends JpaRepository<User, Long> {

	Optional<User> findByIdAndIsDeletedFalse(Long id);

	Optional<User> findByKakaoIdAndIsDeletedFalse(String kakaoId);

	Optional<User> findByKakaoIdAndIsDeletedTrue(String kakaoId);

	boolean existsByKakaoId(String kakaoId);
}

package com.gominitta.backend.domain.user.repository;

import java.time.LocalDateTime;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.gominitta.backend.domain.dailymessage.entity.DailyMessage;
import com.gominitta.backend.domain.user.entity.User;

public interface UserRepository extends JpaRepository<User, Long> {

	Optional<User> findByIdAndIsDeletedFalse(Long id);

	Optional<User> findByKakaoIdAndIsDeletedFalse(String kakaoId);

	Optional<User> findByKakaoIdAndIsDeletedTrue(String kakaoId);

	boolean existsByKakaoId(String kakaoId);

	@Modifying(clearAutomatically = true)
	@Query("UPDATE User u SET u.dailyMessage = :dailyMessage WHERE u.isDeleted = false")
	void updateDailyMessageForAll(@Param("dailyMessage") DailyMessage dailyMessage);

	@Modifying(clearAutomatically = true)
	@Query("DELETE FROM User u WHERE u.isDeleted = true AND u.deletedAt < :cutoff")
	void hardDeleteBefore(@Param("cutoff") LocalDateTime cutoff);
}

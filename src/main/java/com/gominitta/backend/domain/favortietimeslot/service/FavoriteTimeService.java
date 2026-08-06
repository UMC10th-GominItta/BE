package com.gominitta.backend.domain.favortietimeslot.service;


import com.gominitta.backend.domain.favortietimeslot.dto.request.FavoriteTimeRequestDTO;
import com.gominitta.backend.domain.favortietimeslot.dto.response.FavoriteTimeDetailResponseDTO;
import com.gominitta.backend.domain.favortietimeslot.dto.response.FavoriteTimeResponseDTO;
import com.gominitta.backend.domain.favortietimeslot.entity.FavoriteTime;
import com.gominitta.backend.domain.favortietimeslot.exception.FavoriteTimeErrorCode;
import com.gominitta.backend.domain.favortietimeslot.repository.FavoriteTimeRepository;
import com.gominitta.backend.domain.user.entity.User;
import com.gominitta.backend.domain.user.exception.UserErrorCode;
import com.gominitta.backend.domain.user.repository.UserRepository;
import com.gominitta.backend.global.auth.util.SecurityUtil;
import com.gominitta.backend.global.common.exception.GeneralException;
import org.springframework.transaction.annotation.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class FavoriteTimeService {

    private final FavoriteTimeRepository favoriteTimeRepository;
    private final UserRepository userRepository;

    @Transactional
    public FavoriteTimeResponseDTO createFavoriteTime(FavoriteTimeRequestDTO request) {
        Long userId = SecurityUtil.getCurrentUserId();
        User user = userRepository.findByIdAndIsDeletedFalse(userId)
                .orElseThrow(() -> new GeneralException(UserErrorCode.USER_NOT_FOUND));

        FavoriteTime favoriteTime = FavoriteTime.create(user, request.getLabel(), request.getStartTime(), request.getEndTime());
        favoriteTimeRepository.save(favoriteTime);

        return FavoriteTimeResponseDTO.from(favoriteTime);
    }

    @Transactional
    public FavoriteTimeResponseDTO deleteFavoriteTime(Long favoriteTimeId) {
        Long userId = SecurityUtil.getCurrentUserId();
        FavoriteTime favoriteTime = favoriteTimeRepository.findById(favoriteTimeId)
                .orElseThrow(() -> new GeneralException(FavoriteTimeErrorCode.FAVORITETIME_NOT_FOUND));
        if(!favoriteTime.getUser().getId().equals(userId)) {
            throw new GeneralException(FavoriteTimeErrorCode.FORBIDDEN);
        }
        favoriteTimeRepository.delete(favoriteTime);
        return FavoriteTimeResponseDTO.from(favoriteTime);
    }

    @Transactional
    public FavoriteTimeResponseDTO updateFavoriteTime(Long favoriteTimeId,FavoriteTimeRequestDTO request) {
        Long userId = SecurityUtil.getCurrentUserId();
        FavoriteTime favoriteTime = favoriteTimeRepository.findById(favoriteTimeId)
                .orElseThrow(() -> new GeneralException(FavoriteTimeErrorCode.FAVORITETIME_NOT_FOUND));
        if (!favoriteTime.getUser().getId().equals(userId)) {
            throw new GeneralException(FavoriteTimeErrorCode.FORBIDDEN);
        }
        favoriteTime.update(request.getStartTime(), request.getEndTime());
        return FavoriteTimeResponseDTO.from(favoriteTime);
    }

    @Transactional(readOnly = true)
    public FavoriteTimeDetailResponseDTO getFavoriteTime(Long favoriteTimeId) {
        Long userId = SecurityUtil.getCurrentUserId();
        FavoriteTime favoriteTime = favoriteTimeRepository.findById(favoriteTimeId)
                .orElseThrow(() -> new GeneralException(FavoriteTimeErrorCode.FAVORITETIME_NOT_FOUND));
        if (!favoriteTime.getUser().getId().equals(userId)) {
            throw new GeneralException(FavoriteTimeErrorCode.FORBIDDEN);
        }
        return FavoriteTimeDetailResponseDTO.from(favoriteTime);
    }
    @Transactional(readOnly = true)
    public List<FavoriteTimeDetailResponseDTO> getFavoriteTimes() {
        Long userId = SecurityUtil.getCurrentUserId();
        return favoriteTimeRepository.findAllByUser_IdAndIsDeletedFalse(userId).stream()
                .map(FavoriteTimeDetailResponseDTO::from)
                .toList();
    }


}

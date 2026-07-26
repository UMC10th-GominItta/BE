package com.gominitta.backend.domain.worry.service;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.gominitta.backend.domain.session.entity.enums.ThemeCategory;
import com.gominitta.backend.domain.session.service.SessionService;
import com.gominitta.backend.domain.user.entity.User;
import com.gominitta.backend.domain.user.exception.UserErrorCode;
import com.gominitta.backend.domain.user.repository.UserRepository;
import com.gominitta.backend.domain.worry.dto.request.WorryCreateRequestDTO;
import com.gominitta.backend.domain.worry.dto.request.WorryUpdateRequestDTO;
import com.gominitta.backend.domain.worry.dto.response.WorryDetailResponseDTO;
import com.gominitta.backend.domain.worry.dto.response.WorryResponseDTO;
import com.gominitta.backend.domain.worry.entity.Worry;
import com.gominitta.backend.domain.worry.exception.WorryErrorCode;
import com.gominitta.backend.domain.worry.repository.WorryRepository;
import com.gominitta.backend.global.auth.util.SecurityUtil;
import com.gominitta.backend.global.common.exception.GeneralException;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class WorryService {

    private final WorryRepository worryRepository;
    private final UserRepository userRepository;
    private final SessionService sessionService;

    @Transactional
    public WorryResponseDTO createWorry(WorryCreateRequestDTO request) {
        Long userId = SecurityUtil.getCurrentUserId();
        User user = userRepository.findByIdAndIsDeletedFalse(userId)
                .orElseThrow(() -> new GeneralException(UserErrorCode.USER_NOT_FOUND));

        Worry worry = Worry.create(user, request.content(), request.emotionScoreBefore(),
                request.scheduledStartAt(), request.scheduledEndAt());
        worryRepository.save(worry);

        // TODO: AI 임베딩으로 themeCategory 자동 분류 예정
        sessionService.createSessionForWorry(
                worry.getId(), userId,
                worry.getScheduledStartAt(), worry.getScheduledEndAt(),
                worry.getContent(), ThemeCategory.CAREER,
                worry.getEmotionScoreBefore()
        );

        return WorryResponseDTO.from(worry);
    }

    @Transactional(readOnly = true)
    public List<WorryDetailResponseDTO> getWorries() {
        Long userId = SecurityUtil.getCurrentUserId();
        return worryRepository.findAllByUser_IdAndIsDeletedFalse(userId).stream()
                .map(WorryDetailResponseDTO::from)
                .toList();
    }

    @Transactional(readOnly = true)
    public WorryDetailResponseDTO getWorry(Long worryId) {
        Long userId = SecurityUtil.getCurrentUserId();
        Worry worry = worryRepository.findById(worryId)
                .orElseThrow(() -> new GeneralException(WorryErrorCode.WORRY_NOT_FOUND));
        if (!worry.getUser().getId().equals(userId)) {
            throw new GeneralException(WorryErrorCode.FORBIDDEN);
        }
        return WorryDetailResponseDTO.from(worry);
    }

    @Transactional
    public WorryResponseDTO updateWorry(Long worryId, WorryUpdateRequestDTO request) {
        Long userId = SecurityUtil.getCurrentUserId();
        Worry worry = worryRepository.findById(worryId)
                .orElseThrow(() -> new GeneralException(WorryErrorCode.WORRY_NOT_FOUND));
        if (!worry.getUser().getId().equals(userId)) {
            throw new GeneralException(WorryErrorCode.FORBIDDEN);
        }
        worry.update(request.content(), request.scheduledStartAt(), request.scheduledEndAt());
        return WorryResponseDTO.from(worry);
    }

    @Transactional
    public WorryResponseDTO deleteWorry(Long worryId) {
        Long userId = SecurityUtil.getCurrentUserId();
        Worry worry = worryRepository.findById(worryId)
                .orElseThrow(() -> new GeneralException(WorryErrorCode.WORRY_NOT_FOUND));
        if (!worry.getUser().getId().equals(userId)) {
            throw new GeneralException(WorryErrorCode.FORBIDDEN);
        }
        worryRepository.delete(worry);
        return WorryResponseDTO.from(worry);
    }
}

package com.gominitta.backend.domain.favortietimeslot.dto.response;

import com.gominitta.backend.domain.favortietimeslot.entity.FavoriteTime;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;

@Builder
@Schema(description = "즐겨찾는시간 생성/수정/삭제 응답")
public record FavoriteTimeResponseDTO (
        @Schema(description = "즐겨찾는 시간Id")
        Long favoriteTimeId
){
    public static FavoriteTimeResponseDTO from(FavoriteTime favoriteTime) {
        return new FavoriteTimeResponseDTO(favoriteTime.getId());
    }
}


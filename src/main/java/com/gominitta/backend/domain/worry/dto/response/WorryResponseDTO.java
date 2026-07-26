package com.gominitta.backend.domain.worry.dto.response;


import com.gominitta.backend.domain.worry.entity.Worry;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;

@Builder
@Schema(description = "걱정 생성/수정/삭제 완료 응답")
public record WorryResponseDTO (
        @Schema(description = "걱정 Id")
        Long worryId
){
    public static WorryResponseDTO from(Worry worry){
        return new WorryResponseDTO(worry.getId());
    }
}

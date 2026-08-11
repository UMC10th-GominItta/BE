package com.gominitta.backend.domain.worry.dto.request;


import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;

@Builder
@Schema(description = "걱정 한줄추가 (걱정 내용 누적) ")
public record WorryContentRequestDTO (
        @Schema(description = "걱정 내용", example = "걱정걱정걱정")
        String content
){}

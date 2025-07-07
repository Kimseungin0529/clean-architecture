package com.project.doongdoong.domain.analysis.adapter.in.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

@Getter
@NoArgsConstructor
public class FeelingStateResponseListDto {
    private List<FeelingStateResponseDto> feelingStateResponsesDto;

    @Builder
    public FeelingStateResponseListDto(List<FeelingStateResponseDto> feelingStateResponsesDto) {
        this.feelingStateResponsesDto = feelingStateResponsesDto;
    }
}

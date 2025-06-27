package com.project.doongdoong.domain.answer.adapter.in.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class AnswerCreateResponseDto {
    private Long answerId;

    @Builder
    public AnswerCreateResponseDto(Long answerId) {
        this.answerId = answerId;
    }
}
